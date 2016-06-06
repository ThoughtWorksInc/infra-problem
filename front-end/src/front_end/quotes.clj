(ns front-end.quotes
  (:require [common-utils.core :as utils]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [http.async.client :as http]
            ))

(def quote-service-url
  (utils/config "QUOTE_SERVICE_URL" "http://localhost:8080"))

(defn get-quote
  [client]
  (log/debug "get-quote")
  (let [resp (http/await (http/GET client (str quote-service-url "/api/quote")))]
    (if-let [err (http/error resp)]
      (throw err)
      (json/read-str (str (http/body resp))))))

