(ns common-utils.core)

(defn config
  [var-name default]
  (if-let [val (get (System/getenv) var-name)]
    val
    default))
