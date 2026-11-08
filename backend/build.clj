(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'agents-ui/backend)
(def class-dir "target/classes")
(def jar-file (format "target/%s" (name lib)))

(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile '[deploy]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file (str jar-file ".jar")
           :basis @basis
           :main 'deploy}))