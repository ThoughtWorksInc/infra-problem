(ns front-end.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [common-utils.core :as utils]
            [common-utils.middleware :as mw]
            [front-end.quotes :as q]
            [clojure.tools.logging :as log]
            [ring.middleware.reload :refer [wrap-reload]]
            [front-end.utils :refer :all]
            [http.async.client :as http]
            [ring.adapter.jetty :as jetty]))

(defn index []
  (with-open [client (http/create-client)]
    (let [resp (q/get-quote client)]
      (log/debug "Got quote")
      (template "views/templates/home.html" {:quote resp}))))

(defroutes app-routes
  (GET "/" [] (index)))

(def app
  (-> app-routes
      mw/correlation-id-middleware
      wrap-reload))

(defn -main []
  (let [port (:app_port config)]
    (log/info "Running front-end on port" port)
    (future (jetty/run-jetty (var app) {:host "0.0.0.0"
                                        :port port}))))
