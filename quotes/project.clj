(defproject quotes "0.1.0-SNAPSHOT"
  :description "Simple API that returns quotes"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.6.1"]
                 [http-kit "2.4.0-alpha6"]
                 [com.thoughtworks/common-utils "0.1.0-SNAPSHOT"]
                 [org.clojure/tools.logging "0.3.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler quotes.routes/app}
  :main ^:skip-aot quotes.routes
  :target-path "target/%s"
  :resource-paths ["resources/"]
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[peridot "0.5.2"]
                                      [midje "1.9.9"]]
                       :plugins      [[lein-midje "3.2"]]
                       :aliases      {"test" ["midje"]}}
             })
