(ns quotes.integration.api
  (:require [midje.sweet :refer :all]
            [peridot.core :as p]
            [clojure.data.json :as json]
            [quotes.routes :as routes]))

(facts "about 'api'"
       (let [app (p/session routes/app)]
         (fact "returns 200 on quote API and contains correct response"
               (let [{response :response} (p/request app "/api/quote")]
                 (:status response) => 200
                 (get-in response [:headers "Content-Type"]) => "application/json"
                 (contains? (json/read-str (:body response)) "quote") => true
                 (contains? (json/read-str (:body response)) "author") => true
                 ))
         (fact "all requests have a correlation-id header"
               (let [{response :response} (p/request app "/anything")]
                 (empty? (get-in response [:headers "X-Correlation-ID"])) => false))
         (fact "requests pass on the correlation-id header if provided"
               (let [{response :response} (p/request (p/header app "X-Correlation-ID" "test") "/anything")]
                 (get-in response [:headers "X-Correlation-ID"]) => "test"))))
