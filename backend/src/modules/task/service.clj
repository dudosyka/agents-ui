(ns modules.task.service
  (:require [modules.task.dto :as dto]
            [modules.task.model :as dao]
            [utils.validator :refer [validate]]))

(defn create-task
  [ds task-data]
  (print 123)
  (let [validated (validate dto/CreateTaskSpec task-data)
        task (dto/build-task validated)
        created (dao/create-task ds task)]
    created))