(ns utils.agent.codex
  (:require [clojure.data.json :as json]
            [http.async.client :as http]
            [utils.sluj :refer [sluj]]
            [utils.validator :refer [validate]]))


(def CreateAgentProjectSpec
  (:map
   [:name :string]
   [:sources {:optional true} string?]))


(defn- build-project-path [name]
  (str "/Users/dudosyka/Documents/codex-projects/" (sluj name)))


(defn create-project [create-agent-project-spec]
  (let [validated (validate CreateAgentProjectSpec create-agent-project-spec)
        name (:name validated)
        sources (:sources validated)
        validated (assoc validated :sources
                         (if sources [sources] [(build-project-path name)]))]
    (with-open [client (http/create-client)]
      (let [response (http/POST client
                       "http://127.0.0.1:32145/projects"
                       {:body (json/write-str validated) :headers {:content-type "application/json"}})]
        (-> response
            http/await
            http/string
            json/read-json)))))
