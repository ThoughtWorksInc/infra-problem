(ns newsfeed.core
  (:gen-class)
  (:use [newsfeed.api :only [api-routes]])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [common-utils.core :as utils]
            [common-utils.middleware :refer [correlation-id-middleware]]
            [ring.middleware.reload :refer [wrap-reload]]
            [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server]]))

(defroutes app-routes
  (context "/api" []
           (api-routes))
  (route/not-found (json/write-str {:error "Not found"})))

(def app
  (-> app-routes
      correlation-id-middleware
      wrap-reload))

(defn -main []
  (let [port (Integer/parseInt (utils/config "APP_PORT" "8080"))]
    (log/info "Running newsfeed on port" port)
    (run-server (var app) {:ip "0.0.0.0"
                           :port port})))
