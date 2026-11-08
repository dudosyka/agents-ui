(ns core
  (:require [com.stuartsierra.component :as component]
            [components.pg :refer [pg-component]]
            [components.redis :refer [create-redis]]
            [components.ring-server.component :refer [create-server]]))

(defn create-system
  []
  (component/system-map
   :pg-datasource
   (pg-component)

   :redis
   (component/using
    (create-redis)
    [:pg-datasource])

   :http-server-component
   (component/using
    (create-server)
    [:pg-datasource :redis])))
