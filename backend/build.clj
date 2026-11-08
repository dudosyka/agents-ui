(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'agents-ui/backend)
(def version "0.1.0")
(def class-dir "target/classes")
(def jar-file (format "target/%s-%s" (name lib) version))

(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/uber {:uber-file (str jar-file "-uber.jar")
           :class-dir class-dir
           :main "deploy"})
  (b/jar {:class-dir class-dir :jar-file (str jar-file ".jar")}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile '[deploy]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file (str jar-file "-uber.jar")
           :basis @basis
           :main 'deploy}))