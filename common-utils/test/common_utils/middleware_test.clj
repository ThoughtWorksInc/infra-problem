(ns common-utils.middleware-test
  (:require [midje.sweet :refer :all]
            [common-utils.middleware :as mw]))

(defn gen-response
  [request]
  {:status 200
   :body ""
   :headers {}})

(facts "about 'correlation-id-middleware'"
       (let [request-handler (mw/correlation-id-middleware gen-response)
             request {:uri "/"
                      :scheme "http"
                      :request-method "get"
                      :headers {}}]
         (fact "all requests have a correlation-id header"
               (let [response (request-handler request)]
                 (empty? (get-in response [:headers "X-Correlation-ID"])) => false))
         (fact "requests pass on the correlation-id header if provided"
               (let [response (request-handler (assoc-in request [:headers "x-correlation-id"] "test"))]
                 (get-in response [:headers "X-Correlation-ID"]) => "test"))))
