(ns modules.project.model
  (:require [honey.sql.helpers :as h]
            [utils.database :as database]))

(defn create-project
  [ds project-dto]
  (let [{:keys [title description tracker-id closed]} project-dto]
    (database/execute-one! ds {:insert-into [:project]
                               :columns [:title :description :trackerId :closed]
                               :values [[title description tracker-id closed]]
                               :returning :*})))


(defn update-project
  [ds project-dto]
  (let [{:keys [id title description tracker-id closed]} project-dto]
    (database/execute-one! ds {:update [:task]
                               :set {:title title :description description
                                     :trackerId tracker-id :closed closed}
                               :where [:= :id id]
                               :returning :*})))


(defn delete-project
  [ds project-id]
  (database/execute-one! ds {:delete-from [:task]
                             :where [:= :id project-id]}))


(defn find-one
  [ds project-id]
  (database/execute-one! ds {:select [:*] :from [:task] :where [:= :id project-id]}))


(defn find-all
  [ds filters]
  (let [{:keys [text]} filters
        escaped (format "%%%s%%" text)
        query (-> (h/select :*)
                  (h/from :project)
                  (cond->
                   text (h/where [:or
                                  [:ilike :title escaped]
                                  [:ilike :description escaped]
                                  [:ilike :trackerId escaped]])))]
    (database/execute! ds query)))

;; (sql/format (find-all nil {:project 123 :status "test" :text "anything"}))