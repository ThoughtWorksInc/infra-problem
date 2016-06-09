(ns newsfeed.api
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [feedparser-clj.core :as feed]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [ring.util.response :refer :all]))

(def options {:timeout 1000
              :user-agent "Newsfeed"})

(def feed-urls [;;"https://news.ycombinator.com/rss"
                "http://www.martinfowler.com/feed.atom"
                "https://www.thoughtworks.com/rss/insights.xml"])

(defn get-feeds
  [urls]
  (->> urls
      (map #(http/get % options))
      (map deref)
      (map :body)))

(defn get-sort-value
  [item]
  (if (not (nil? (:updated-date item)))
    (:updated-date item)
    (:published-date item)))

(defn combine-feeds
  [feeds]
  (reverse (sort-by get-sort-value (apply concat (map :entries feeds)))))

(defn process-feed
  [feed]
  (assoc-in feed [:entries] (map #(assoc-in % [:source] {:title (:title feed)
                                                         :link  (:link feed)
                                                         :image (:image feed)})
                                 (:entries feed))))


(defn fetch-parse-combine []
  (let [feeds (get-feeds feed-urls)]
    (->> feeds
         (map #(.getBytes %))
         (map clojure.java.io/input-stream)
         (map feed/parse-feed)
         (map process-feed)
         (combine-feeds))))

(defn format-key-value
  [key value]
  (if (instance? java.util.Date value)
    (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss.SSSZ") value)
    value))

(defn api-routes []
  (routes
    (GET "/feeds" [] (json/write-str {:entries (fetch-parse-combine)}
                                     :value-fn format-key-value))))
