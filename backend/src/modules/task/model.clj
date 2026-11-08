(ns modules.task.model
  (:require [honey.sql.helpers :as h]
            [utils.database :as database]))

(defn create-task
  [ds task-dto]
  (let [{:keys [gpt-uuid project-id title
                description tracker-id status
                closed feedback retro-processed]} task-dto]
    (database/execute-one! ds {:insert-into [:task]
                               :columns [:gptUuid :projectId :title
                                         :description :trackerId :status
                                         :closed :feedback :retro_processed]
                               :values [[gpt-uuid project-id title
                                         description tracker-id status
                                         closed feedback retro-processed]]
                               :returning :*})))


(defn update-task
  [ds task-dto]
  (let [{:keys [id gpt-uuid project-id title
                description tracker-id status
                closed feedback retro-processed]} task-dto]
    (database/execute-one! ds {:update [:task]
                               :set {:gptUuid gpt-uuid :projectId project-id
                                     :title title :description description
                                     :trackerId tracker-id :status status
                                     :closed closed :feedback feedback
                                     :retro_processed retro-processed}
                               :where [:= :id id]
                               :returning :*})))


(defn delete-task
  [ds task-id]
  (database/execute-one! ds {:delete-from [:task]
                             :where [:= :id task-id]}))


(defn find-one
  [ds task-id]
  (database/execute-one! ds {:select [:*] :from [:task] :where [:= :id task-id]}))


(defn find-all
  [ds filters]
  (let [{:keys [project status text]} filters
        escaped (format "%%%s%%" text)
        query (-> (h/select :task.*)
                  (h/from :task)
                  (h/left-join [:project] [:= :project.id :task.id])
                  (cond->
                   project (h/where := :project.id project)
                   status (h/where := :task.status status)
                   text (h/where [:or
                                  [:ilike :task.title escaped]
                                  [:ilike :task.description escaped]
                                  [:ilike :task.trackerId escaped]])))]
    (database/execute! ds query)))

;; (sql/format (find-all nil {:project 123 :status "test" :text "anything"}))