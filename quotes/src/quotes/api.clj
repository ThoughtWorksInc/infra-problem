(ns quotes.api
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [ring.util.response :refer :all]))

(def quotes (json/read-str (slurp (io/resource "quotes.json"))))

(defn get-quote []
  (-> (response (json/write-str (rand-nth quotes)))
      (content-type "application/json")))

(defn api-routes []
  (routes
    (GET "/quote" [] (get-quote))))
