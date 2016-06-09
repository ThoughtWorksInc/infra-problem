(ns front-end.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [common-utils.core :as utils]
            [common-utils.middleware :as mw]
            [front-end.data :as d]
            [clojure.tools.logging :as log]
            [ring.middleware.reload :refer [wrap-reload]]
            [front-end.utils :refer :all]
            [ring.adapter.jetty :as jetty]))

(defn index []
  (let [q (d/get-quote)
        n (d/get-news)]
    (template "views/templates/home.html" {:quote (d/handle-quote-response q)
                                           :news  (d/handle-news-response n)})))

(defroutes app-routes
  (GET "/" [] (index))
  (route/not-found "<h1>Not found</h1>"))

(def app
  (-> app-routes
      mw/correlation-id-middleware
      wrap-reload))

(defn -main []
  (let [port (:app_port config)]
    (log/info "Running front-end on port" port)
    (future (jetty/run-jetty (var app) {:host "0.0.0.0"
                                        :port port}))))
