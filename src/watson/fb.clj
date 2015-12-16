(ns watson.fb
  (:gen-class)
  (:require [clj-http.lite.client :as client]
            [watson.ws :refer [iskyle?]]))


(def auth-token
  (atom nil))



(defn fb-reply
  [msg-txt user]
  (if (iskyle? user)
  (let [matches (re-matches #"(watson post to the facebook group) (.+)" msg-txt) post-content (get matches 2)]
    (client/post "https://graph.facebook.com/v2.5/320059841389098/feed" {:form-params {:message post-content :access_token (deref auth-token)}})
    "Message posted to the SigApp Facebook Page")
  "Sorry you are not authorized to use this command"))

(defn set-token-reply
  [msg-text user]
  (if (iskyle? user)
    (let [matches (re-matches #"(watson set fb auth token) (.+)" msg-text) token (get matches 2)]
      (println token)
      (swap! auth-token (fn [a] token))
      (str "FB Auth Token set to " (deref auth-token)))
    "Sorry you are not authorized to use this command"))
   
