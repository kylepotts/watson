(ns watson.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [watson.ws :refer [watson-id user-cache channels-cache groups-cache connect-to-slack snd-msg]]
            [watson.statuscmd :refer [status-reply]]
            [watson.fb :refer [fb-reply set-token-reply]]
            [watson.hn :refer [hn-reply]]))

(declare handle-msg)

(def reg-terms
  (list {:pattern (re-pattern "watson status") :function status-reply}
        {:pattern (re-pattern "watson post to the facebook group") :function fb-reply}
        {:pattern (re-pattern "watson set fb auth token .*") :function set-token-reply}
        {:pattern (re-pattern "watson hn") :function hn-reply}))


(defn on-recv
  [msg]
  (let [m (json/read-str msg)]
    (if (= "message" (m "type"))
      (do
        (println m)
        (handle-msg m)))))


(def ws
  (connect-to-slack on-recv))

(defn handle-msg
  [msg]
  (let [msg-txt (msg "text") msg-channel (msg "channel") user (msg "user")]
    (doseq [pair reg-terms]
      (if (not= nil (re-find (pair :pattern) msg-txt))
        (let [txt ((pair :function) msg-txt user)]
          (snd-msg txt msg-channel ))))))




(defn -main
  []
  (println "starting")
  )
