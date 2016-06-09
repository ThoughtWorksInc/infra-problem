(ns front-end.unit.data
  (:require [midje.sweet :refer :all]
            [clojure.data.json :as json]
            [front-end.data :as data]))

(def good-news-response
  {:body (json/write-str {:entries [{:published-date "2015-01-01T00:00:00.000+0000"
                                     :title "Title 1"}]})
   :status 200})

(def bad-response
  {:body "Bad response"
   :status 500})

(facts "about 'data'"
       (fact "handle-quote-response catches errors"
             (data/handle-quote-response (future bad-response)) => (throws Exception "Error getting response: status 500 returned"))
       (fact "handle-news-response catches errors"
             (data/handle-news-response (future bad-response)) => (throws Exception "Error getting response: status 500 returned"))
       (fact "handle-news-response correctly processes entries"
             (let [d (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd") "2015-01-01T00:00:00.000+0000")]
               (data/handle-news-response (future good-news-response))
               => [{"published-date" d
                    "title"          "Title 1"
                    "date"           d}])))
