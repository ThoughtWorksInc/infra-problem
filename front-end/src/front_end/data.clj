(ns front-end.data
  (:require [common-utils.core :as utils]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [org.httpkit.client :as http]))

(def quote-service-url
  (utils/config "QUOTE_SERVICE_URL" "http://localhost:8080"))

(def newsfeed-service-url
  (utils/config "NEWSFEED_SERVICE_URL" "http://localhost:8080"))

(def options {:as         :text
              :timeout    1000
              :user-agent "front-end"})

(defn- handle-response
  [resp]
  (let [status (:status @resp)]
    (if (and (>= 200 status)
             (< status 300))
      @resp
      (throw (Exception. (str "Error getting response: status " status " returned"))))))

(defn get-quote []
  (http/get (str quote-service-url "/api/quote") options))

(defn handle-quote-response
  [resp]
  (json/read-str (:body (handle-response resp))))

(defn get-news []
  (http/get (str newsfeed-service-url "/api/feeds") options))

(defn- handle-news-values
  [key value]
  (if (and (not (nil? value))
           (.contains key "date"))
    (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss.SSSZ") value)
    value))

(defn- process-entry
  [entry]
  (log/info "processing")
  (let [date (or (get entry "updated-date")
                 (get entry "published-date"))]
    (log/debug entry date)
    (assoc-in entry ["date"] date)))

(defn handle-news-response
  [resp]
  (let [body    (:body (handle-response resp))
        r       (json/read-str body :value-fn handle-news-values)
        entries (get r "entries")]
    (map process-entry entries)))
