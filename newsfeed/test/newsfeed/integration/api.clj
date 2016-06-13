(ns newsfeed.integration.api
  (:require [midje.sweet :refer :all]
            [peridot.core :as p]
            [clojure.data.json :as json]
            [newsfeed.core :as nf]
            [newsfeed.api :as api]))

(facts "about 'api'"
       (let [app (p/session nf/app)]
         (fact "returns 200 on /ping"
               (get-in (p/request app "/ping") [:response :status]) => 200)
         (fact "returns 403 when no token given"
               (get-in (p/request app "/api") [:response :status]) => 403)
         (fact "returns 403 when invalid token given"
               (get-in (p/request app "/api" :headers {"X-Auth-Token" "invalid"}) [:response :status]) => 403)
         (fact "returns 404 when valid token given but invalid route"
               (get-in (p/request app "/api/404" :headers {"X-Auth-Token" "valid"}) [:response :status]) => 404
               (provided (contains? nf/tokens "valid") => true))
         (fact "returns 200 on /api/feeds when valid token given"
               (get-in (p/request app "/api/feeds" :headers {"X-Auth-Token" "valid"}) [:response :status]) => 200
               (provided (contains? nf/tokens "valid") => true
                         (api/get-feeds anything) => '())))) ; fake the feed calls
