(ns handlers)

(defn contacts-handler [request]
  (println "Invoking contacts-handler")
  (clojure.pprint/pprint request)
  (let [query (get (:params request) "q")]
    (if query (str "Contact: " query)
        "All Contacts")))