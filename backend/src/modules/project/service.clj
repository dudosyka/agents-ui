(ns modules.project.service
  (:require [modules.project.dto :as dto]
            [modules.project.model :as dao]
            [utils.agent.codex :as codex]
            [utils.validator :refer [validate]]))

(defn create-project
  [ds project-data]
  (let [validated (validate dto/CreateProjectSpec project-data)
        task (dto/build-project validated)
        created (dao/create-project ds task)
        name (:title validated)]
    (codex/create-project {:name name})
    created))
