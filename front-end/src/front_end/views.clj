(ns front-end.views
  (:require [hiccup.page :refer :all]
            [clojure.string :as str]
            [front-end.utils :as utils]))

(defn- format-date
  ([date] (format-date date "yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
  ([date fmt]
   (if (instance? java.util.Date date)
     (.format (java.text.SimpleDateFormat. fmt) date)
     date)))

(defn- base
  [& content]
  (let [cfg utils/config]
    (html5 [:head [:meta {:charset "UTF8"}]
            [:meta {:name "viewport"
                    :content "width=device-width, initial-scale=1"}]
            [:title "Newsfeed"]
            (include-css (str (:static_path cfg) "/css/bootstrap.min.css"))]
           [:body [:div.container content]])))

(defn error []
  (base [:h1 "Internal error"]
        [:div.row [:div.alert.alert-danger "An error occurred when processing your request. Please try again later"]]))

(defn not-found []
  (base [:h1 "Not found"]
        [:div.row [:div.alert.alert-danger "Sorry but we couldn't find the page you were looking for."]]))

(defn index
  [quot news]
  (base [:h1 "Newsfeed"]
        [:div.row
         [:div.col-md-8.newsfeed
          [:ol.list-unstyled
           (for [item news]
             [:li
              [:time.text-muted.pull-right {:datetime (format-date (get item "date"))}
               (format-date (get item "date") "yyyy-MM-dd HH:mm")]
              [:h3 [:a {:href (get item "link")} (get item "title")]]
              [:cite
               (if-let [authors (not-empty (get item "authors"))]
                 (str (str/join ", " (map #(get % "name") authors)) " for "))
               [:a {:href (get (get item "source") "link")} (get (get item "source") "title")]]])]]
         [:div.col-md-4
          [:blockquote
           (map #(vector :p %) (str/split (get quot "quote") #"\n"))
           [:footer (get quot "author")]]]]))
