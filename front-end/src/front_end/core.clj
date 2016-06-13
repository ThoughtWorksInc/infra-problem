(ns front-end.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [common-utils.core :as utils]
            [common-utils.middleware :as mw]
            [front-end.data :as d]
            [front-end.views :as views]
            [front-end.utils :refer :all]
            [clojure.tools.logging :as log]
            [ring.middleware.reload :refer [wrap-reload]]
            [org.httpkit.server :refer [run-server]]))

(defn index []
  (let [q (d/get-quote)
        n (d/get-news)]
    (views/index (d/handle-quote-response q)
                 (d/handle-news-response n))))

(defn- wrap-exception-handling
  [app]
  (fn [request]
    (try (app request)
         (catch Exception e (do (log/error e (str (clojure.string/upper-case (name (:request-method request))) " " (:uri request)))
                                {:status 500
                                 :body   (views/error)})))))

(defroutes app-routes
  (GET "/ping" [] {:status 200})
  (GET "/" [] (index))
  (route/not-found {:status 404
                    :body   (views/not-found)}))

(def app
  (-> app-routes
      mw/correlation-id-middleware
      wrap-exception-handling
      wrap-reload))

(defn -main []
  (let [port (:app_port config)]
    (log/info "Running front-end on port" port)
    (run-server (var app) {:ip "0.0.0.0"
                           :port port})))
