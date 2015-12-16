(ns watson.hn
  (:gen-class)
  (:require [clj-http.lite.client :as client]
            [clojure.data.json :as json]))

(defn get-hn-top
  []
  (let [resp (json/read-str ((client/get "https://hacker-news.firebaseio.com/v0/topstories.json") :body))]
    (take 5 resp)))


(defn get-hn-article
  [id]
  (let [url (str "https://hacker-news.firebaseio.com/v0/item/" id ".json") article (json/read-str ((client/get url) :body))]
    article))

(defn article-to-str
  [article]
  (str (article "title") "   " (article "url") "\n\n"))

(defn article-reply
  [id]
  (article-to-str (get-hn-article id)))
  

(defn hn-reply
  [msg-txt user]
  (let [ids (get-hn-top) articles (map #(article-reply %) ids) ]
    (str "```" (apply str articles) "```")))


    




