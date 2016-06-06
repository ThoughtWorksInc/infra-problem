(ns quotes.routes
  (:gen-class)
  (:use [quotes.api :only [api-routes]])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [common-utils.core :as utils]
            [common-utils.middleware :refer [correlation-id-middleware]]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as jetty]))

(defroutes app-routes
  (context "/api" []
           (api-routes)))

(def app
  (-> app-routes
      correlation-id-middleware))

(defn -main []
  (let [port (Integer/parseInt (utils/config "APP_PORT" 8080))]
    (log/info "Running quotes on port" port)
    (future (jetty/run-jetty (var app) {:port port}))))
