(ns watson.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [watson.ws :refer [watson-id user-cache channels-cache groups-cache connect-to-slack snd-msg]]
            [watson.statuscmd :refer [status-reply]]
            [watson.fb :refer [fb-reply]]))

(declare handle-msg)

(def reg-terms
  (list {:pattern (re-pattern "watson status") :function status-reply}
        {:pattern (re-pattern "watson post to the facebook group") :function fb-reply}))


(defn on-recv
  [msg]
  (let [m (json/read-str msg)]
    (if (= "message" (m "type"))
      (handle-msg msg))))


(def ws
  (connect-to-slack on-recv))

(defn handle-msg
  [msg]
  (let [m (json/read-str msg) msg-txt (m "text")]
    (doseq [pair reg-terms]
      (if (not= nil (re-find (pair :pattern) msg-txt))
        (let [txt ((pair :function)) channel (m "channel")]
          (snd-msg txt channel ))))))




(defn -main
  []
  (println "starting")
  )
