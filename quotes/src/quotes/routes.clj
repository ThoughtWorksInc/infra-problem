(ns quotes.routes
  (:gen-class)
  (:use [quotes.api :only [api-routes]])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]))

(defn get-or-generate-correlation-id
  [request]
  (get-in request
          [:headers  "x-correlation-id"]
          (str (java.util.UUID/randomUUID))))

(defn correlation-id-middleware [app]
  (fn [request]
    (let [correlation-id (get-or-generate-correlation-id request)]
      (let [response (app (assoc-in request [:headers "x-correlation-id"] correlation-id))]
        (assoc-in response [:headers "X-Correlation-ID"] correlation-id)))))

(defroutes app-routes
  (context "/api" []
           (api-routes)))

(def app
  (-> app-routes
      correlation-id-middleware))

(defn -main []
  (future (jetty/run-jetty (var app) {:port 8080})))
