(ns front-end.utils
  (:require [common-utils.core :as utils]
            [selmer.parser :refer :all]))

(def config
  {:static_path (utils/config "STATIC_URL" "")
   :app_port (Integer/parseInt (utils/config "APP_PORT" "8080"))})

;; Turn the template cache off if we're in debug mode
(if (not (empty? (utils/config "DEBUG" nil)))
  (cache-off!))

(defn template
  [template params]
  (render-file template (merge {:config config}
                               params)))
