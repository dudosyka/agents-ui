(ns components.pg
  (:require [clojure.tools.logging :as log]
            [components.config :refer [fetch-config]]
            [next.jdbc.connection :as connection])
  (:import (org.flywaydb.core Flyway)))

(defn pg-component
  []
  (connection/component
   com.zaxxer.hikari.HikariDataSource
   (assoc (:database (fetch-config))
          :init-fn (fn [datasource]
                     (log/info "Database initializer run (migration)")
                     (.migrate
                      (.. (Flyway/configure)
                          (dataSource datasource)
                          ; https://www.red-gate.com/blog/database-devops/flyway-naming-patterns-matter
                          (locations (into-array String ["classpath:database/migrations"]))
                          (table "schema_version")
                          (load)))))))
