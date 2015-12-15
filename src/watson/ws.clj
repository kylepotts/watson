(ns watson.ws
  (:gen-class)
  (:require [environ.core :refer [env]]
            [clj-http.lite.client :as client]
            [clojure.data.json :as json]
            [gniazdo.core :as wss]))



(def slackkey
  (env :watson-slack-key))

(def slack-rtm-start-url
  (str "https://slack.com/api/rtm.start" "?" "token=" slackkey))

(def watson-id
  (atom nil))

(def user-cache
  (atom nil))

(def channels-cache
  (atom nil))

(def groups-cache
  (atom nil))

(def ws-url
  (atom nil))

(def ws
  (atom nil))

(defn on-recv
  [msg]
  (println msg))

(defn snd-msg
  [msg-txt channel]
  (let [resp {:id (deref watson-id) :type "message" :channel channel :text msg-txt}]
    (wss/send-msg (deref ws) (json/write-str resp))))

(defn connect-ws
  [recv-fn]
  (let [url (deref ws-url)]
    (wss/connect
      url
      :on-connect (fn [a] (println "WS Connected!"))
      :on-receive recv-fn)))

(defn get-rtm-ws-url
  [on-recv-fn]
  (let [resp (json/read-str(get (client/get slack-rtm-start-url) :body))]
    (swap! watson-id (fn [a] (get-in resp ["self" "id"])))
    (swap! user-cache (fn [a] (get-in resp ["users"])))
    (swap! channels-cache (fn [a] (get-in resp ["channels"])))
    (swap! groups-cache (fn [a] (get-in resp ["groups"])))
    (swap! ws-url (fn [a] (get-in resp ["url"])))
    (swap! ws (fn [a] (connect-ws on-recv-fn)))))

(defn connect-to-slack
  [on-recv-fn]
  (get-rtm-ws-url on-recv-fn)
  (deref ws))

