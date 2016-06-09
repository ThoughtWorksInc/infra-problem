(ns front-end.utils
  (:require [common-utils.core :as utils]))

(def config
  {:static_path (utils/config "STATIC_URL" "")
   :app_port (Integer/parseInt (utils/config "APP_PORT" "8080"))})
