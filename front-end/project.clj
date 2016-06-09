(defproject front-end "0.1.0-SNAPSHOT"
  :description "Front-end webapp for assessment"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [com.thoughtworks/common-utils "0.1.0-SNAPSHOT"]
                 [compojure "1.5.0"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.18"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [ring/ring-devel "1.4.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler front-end.core/app-routes}
  :main ^:skip-aot front-end.core
  :target-path "target/%s"
  :resources-paths ["resources/"]
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[peridot "0.4.3"]
                                      [midje "1.8.3"]]
                       :plugins      [[lein-midje "3.2"]]
                       :aliases      {"test" ["midje"]}}
             })
