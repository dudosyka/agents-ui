(ns modules.project.controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST]]
            [modules.project.service :as service]
            [ring.util.response :refer [response]]))

(defn prefixed [url] (str "/api/v1/project" url))

(defn routes []
  [(POST (prefixed "") request
     (let [{:keys [pg-datasource]} (:deps request)
           data (:params request)]
       (print 123)
       (->> data
            (service/create-project pg-datasource)
            (json/write-str)
            response)))])