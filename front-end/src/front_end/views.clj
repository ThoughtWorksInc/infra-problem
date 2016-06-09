(ns front-end.views
  (:require [hiccup.core :refer :all]
            [clojure.string :as str]
            [front-end.utils :as utils]))

(defn- format-date
  ([date] (format-date date "yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
  ([date fmt]
   (if (instance? java.util.Date date)
     (.format (java.text.SimpleDateFormat. fmt) date)
     date)))

(defn index
  [quot news]
  (let [cfg utils/config]
    (html [:head [:meta {:charset "UTF8"}]
                 [:meta {:name "viewport"
                         :content "width=device-width, initial-scale=1"}]
                 [:title "Newsfeed"]
                 [:link {:rel "stylesheet"
                         :type "text/css"
                         :href (str (:static_path cfg) "/css/bootstrap.min.css")}]]
          [:body [:div.container
                  [:h1 "Newsfeed"]
                  [:div.row
                   [:div.col-md-8.newsfeed
                    [:ol.list-unstyled
                     (for [item news]
                       [:li
                        [:h3 [:a {:href (get item "link")} (get item "title")]]
                        [:time {:datetime (format-date (get item "date"))} (format-date (get item "date") "yyyy-MM-dd HH:mm")]
                        [:cite [:a {:href (get (get item "source") "link")} (get (get item "source") "title")]]])]]
                   [:div.col-md-4
                    [:blockquote
                     (vec (interleave (repeat :p) (str/split (get quot "quote") #"\n")))
                     [:footer (get quot "author")]]]]]])))
