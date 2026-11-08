(ns modules.task.controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST]]
            [modules.task.service :as service]
            [ring.util.response :refer [response]]))

(defn prefixed [url] (str "/api/v1/task" url))

(defn routes []
  [(POST (prefixed "") request
     (let [{:keys [pg-datasource]} (:deps request)
           data (:params request)]
       (->> data
            (service/create-task pg-datasource)
            (json/write-str)
            response)))])