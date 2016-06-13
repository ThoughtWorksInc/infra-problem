(ns front-end.integration.core
  (:require [midje.sweet :refer :all]
            [peridot.core :as p]
            [clojure.data.json :as json]
            [front-end.core :as fe]))

(facts "about 'front-end'"
       (let [app (p/session fe/app)]
         (fact "returns 200 on /ping"
               (get-in (p/request app "/ping") [:response :status]) => 200)))
