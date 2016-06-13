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

(def tokens #{"T1&eWbYXNWG1w1^YGKDPxAWJ@^et^&kX"})

(defn- valid-token?
  [token]
  (when (contains? tokens token)
    token))

(defn- token-auth
  [app]
  (fn [request]
    (if-let [token (valid-token? (get-in request [:headers "x-auth-token"]))]
      (app request)
      {:status 403
       :body   (json/write-str {:error "Invalid auth token"})})))

(defroutes app-routes
  (GET "/ping" [] {:status 200})
  (context "/api" []
           (token-auth (api-routes)))
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
