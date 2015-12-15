(ns watson.fb
  (:gen-class)
  (:use [clj-facebook-graph.auth])
  (:require [fb-graph-api :as oauth]))

(defn fb-reply
  []
  "Testing fb access")