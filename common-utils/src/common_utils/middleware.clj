(ns common-utils.middleware)

(defn- get-or-generate-correlation-id
  [request]
  (get-in request
          [:headers  "x-correlation-id"]
          (str (java.util.UUID/randomUUID))))

(defn correlation-id-middleware [app]
  (fn [request]
    (let [correlation-id (get-or-generate-correlation-id request)]
      (let [response (app (assoc-in request [:headers "x-correlation-id"] correlation-id))]
        (assoc-in response [:headers "X-Correlation-ID"] correlation-id)))))

