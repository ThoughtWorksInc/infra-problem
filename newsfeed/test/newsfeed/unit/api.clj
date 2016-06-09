(ns newsfeed.unit.api
  (:use org.httpkit.fake)
  (:require [midje.sweet :refer :all]
            [newsfeed.api :as api]))

(unfinished feed-urls get-feeds)

(def good-feed
  "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<rss version=\"2.0\"><channel><title>Test</title><item><title>Item 1</title><guid>/item1</guid></item></channel></rss>")

(facts "about 'api'"
       (with-fake-http [...feed1... "resp1"
                        ...feed2... "resp2"]
         (fact "get-feeds returns correct responses"
               (api/get-feeds [...feed1... ...feed2...]) => ["resp1" "resp2"]))
       (with-fake-http [...feed1... "resp1"
                        ...feed2... 500]
         (fact "get-feeds handles failures gracefully"
               (api/get-feeds [...feed1... ...feed2...]) => ["resp1"]))
       (fact "combine-feeds successfully combines and sorts"
             (api/combine-feeds [{:entries [...entry1... ...entry2...]}
                                 {:entries [...entry3... ...entry4...]}])
             => [...entry4... ...entry3... ...entry2... ...entry1...])
       (fact "fetch-parse-combine handles failures gracefully"
             (api/fetch-parse-combine) => (one-of map?) ;; We don't care what the content is
             (provided (api/get-feeds anything) => ["<html><body><h1>bad feed</h1></body></html>" good-feed])))

(facts "about 'formatting'"
       (fact "given various values will format correctly"
             (api/format-key-value :published-date (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd") "2016-01-01"))
             => "2016-01-01T00:00:00.000+0000"
             (api/format-key-value :title "Title")
             => "Title"))

