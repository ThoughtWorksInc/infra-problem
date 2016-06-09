(ns newsfeed.unit.api
  (:use org.httpkit.fake)
  (:require [midje.sweet :refer :all]
            [newsfeed.api :as api]))

(facts "about 'api'"
       (with-fake-http [...feed1... "resp1"
                        ...feed2... "resp2"]
         (fact "get-feeds returns correct responses"
               (api/get-feeds [...feed1... ...feed2...]) => ["resp1" "resp2"]))
       (fact "combine-feeds successfully combines and sorts"
             (api/combine-feeds [{:entries [...entry1... ...entry2...]}
                                 {:entries [...entry3... ...entry4...]}])
             => [...entry4... ...entry3... ...entry2... ...entry1...]))

(facts "about 'formatting'"
       (fact "given various values will format correctly"
             (api/format-key-value :published-date (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd") "2016-01-01"))
             => "2016-01-01T00:00:00.000+0000"
             (api/format-key-value :title "Title")
             => "Title"))

