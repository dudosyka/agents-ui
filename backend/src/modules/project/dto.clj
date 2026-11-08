(ns modules.project.dto)

(defrecord Project [id title description tracker-id closed])


(def CreateProjectSpec
  [:map
   [:title :string]
   [:description :string]
   [:tracker-id {:optional true} string?]])


(defn build-project [create-project-spec]
  (let [{:keys [title description tracker-id]} create-project-spec]
    (->Project nil title description tracker-id false)))