(ns modules.task.dto)

(defrecord Task [id gpt-uuid project-id title
                 description tracker-id status
                 closed feedback retro-processed])


(def CreateTaskSpec
  [:map
   [:project-id :int]
   [:title :string]
   [:description :string]
   [:tracker-id {:optional true} string?]
   [:status :string]])


(defn build-task [create-task-spec]
  (let [{:keys [gpt-uuid project-id title
                description tracker-id status]} create-task-spec]
    (->Task nil gpt-uuid project-id title
            description tracker-id status
            false "" false)))