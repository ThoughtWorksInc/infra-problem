(ns front-end.utils
  (:require [http.async.client :as http]
            [clojure.tools.logging :as log]))

(defn config
  [var-name default]
  (if-let [val (get (System/getenv) var-name)]
    val
    default))
