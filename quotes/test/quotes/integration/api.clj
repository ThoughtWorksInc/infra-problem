(ns quotes.integration.api
  (:require [midje.sweet :refer :all]
            [peridot.core :as p]
            [clojure.data.json :as json]
            [quotes.routes :as routes]))

(facts "about 'api'"
       (let [app (p/session routes/app)]
         (fact "returns 200 on /ping"
               (get-in (p/request app "/ping") [:response :status]) => 200)
         (fact "returns 200 on quote API and contains correct response"
               (let [{response :response} (p/request app "/api/quote")
                     json-response (json/read-str (:body response))]
                 (:status response) => 200
                 (get-in response [:headers "Content-Type"]) => "application/json"
                 (contains? json-response "quote") => true
                 (contains? json-response "author") => true
                 ))))
