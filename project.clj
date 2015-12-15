(defproject watson "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http-lite "0.3.0"]
                 [environ "1.0.1"]
                 [org.clojure/data.json "0.2.6"]
                 [stylefruits/gniazdo "0.4.1"]]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [watson.core]
  :main watson.core)
