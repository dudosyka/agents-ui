(ns dev
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [core :refer [create-system]]))

(def system (atom (component/start-system (create-system))))
(log/info "System started")
(defn reload-system []
  (reset! system (-> @system
                     (component/stop-system)
                     (component/start-system))))

;; (reload-system)

;; (-> @system;; 
;;     :redis
;;     :redis
;;     (redis/set "key" "value")
;  )
;; 
;; (clojure.pprint/pprint (-> @system :pg-datasource (db/execute! "select 1;")))
;; 
;; (-> @system
;;     :pg-datasource
;; )
;(def debug-source (component/start (datasource-component)))

;(reload-system)