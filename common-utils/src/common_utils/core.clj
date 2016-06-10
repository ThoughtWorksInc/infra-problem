(ns common-utils.core)

(defn config
  "Gets a configuration value from the environment"
  [var-name default]
  (if-let [val (get (System/getenv) var-name)]
    val
    default))
