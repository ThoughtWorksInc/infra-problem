(ns front-end.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [front-end.utils :as utils]
            [front-end.quotes :as q]
            [clojure.tools.logging :as log]
            [ring.middleware.reload :refer [wrap-reload]]
            [selmer.parser :refer :all]
            [http.async.client :as http]
            [ring.adapter.jetty :as jetty]))

(defn index []
  (print "Getting quote")
  (with-open [client (http/create-client)]
    (let [resp (q/get-quote client)]
      (log/debug "Got quote")
      (render-file "views/templates/home.html" {:quote resp}))))

(defroutes app-routes
  (GET "/" [] (index)))

(def app
  (-> app-routes
      wrap-reload))

(defn -main []
  (future (jetty/run-jetty (var app) {:port 8090})))
