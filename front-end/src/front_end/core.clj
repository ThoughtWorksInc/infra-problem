(ns front-end.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [common-utils.core :as utils]
            [common-utils.middleware :as mw]
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
      mw/correlation-id-middleware
      wrap-reload))

(defn -main []
  (let [port (Integer/parseInt (utils/config "APP_PORT" 8080))]
    (log/info "Running front-end on port" port)
    (future (jetty/run-jetty (var app) {:port port}))))
