(defproject newsfeed "0.1.0-SNAPSHOT"
  :description "Simple API that aggregates RSS feeds"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.5.0"]
                 [com.thoughtworks/common-utils "0.1.0-SNAPSHOT"]
                 [http-kit "2.3.0"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [ring/ring-devel "1.4.0"]]
  :main ^:skip-aot newsfeed.core
  :target-path "target/%s"
  :resource-paths ["resources/"]
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[peridot "0.4.3"]
                                      [http-kit.fake "0.2.1"]
                                      [midje "1.8.3"]]
                       :plugins      [[lein-midje "3.2"]]
                       :aliases      {"test" ["midje"]}}
             :midje   {:resource-paths ["test/resources/"]}
             })
