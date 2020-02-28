(defproject com.thoughtworks/common-utils "0.1.0-SNAPSHOT"
  :description "Common library for apps"
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[midje "1.9.9"]]
                       :plugins      [[lein-midje "3.2"]]
                       :aliases      {"test" ["midje"]}}
             })
