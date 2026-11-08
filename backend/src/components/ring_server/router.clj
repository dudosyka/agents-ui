(ns components.ring-server.router
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer [routes]]
            [components.ring-server.middleware :as middlewares]
            [iapetos.collector.ring :as prometheus-ring]
            [iapetos.core :as prometheus]
            [modules.project.controller :as project]
            [modules.task.controller :as task]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.params :refer [wrap-params]]
            [utils.ws :as ws]))

(defonce registry
  (-> (prometheus/collector-registry)
      (prometheus-ring/initialize)))

(def auth-excluded
  [#"\/api\/v1\/.*"])

(defn app-routes [component]
  (-> routes
      (apply (concat '()
                     (task/routes)
                     (project/routes)
                     [(ws/create-ws-endpoint "/ws" ;Endpoint
                                             {} ;Router
                                             [(ws/wrap-exception-handling)
                                              (ws/wrap-jwt-auth auth-excluded) ;Middlewares
                                              (middlewares/wrap-deps-ws component)]
                                             (fn [_ _] ;clients args
                                               (log/info "closed")))]))
      (middlewares/wrap-exceptions-handling)
      (middlewares/wrap-jwt-guard auth-excluded)
      (middlewares/wrap-deps component)
      (prometheus-ring/wrap-metrics registry {:path "/metrics" :exception-status 500})
      (wrap-keyword-params)
      (wrap-params)
      (wrap-multipart-params)
      (wrap-json-params)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete :patch])
      (middlewares/wrap-content-type-json)
      (middlewares/wrap-not-found)
      (middlewares/wrap-request-logging)))
