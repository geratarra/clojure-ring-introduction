(ns handlers
  (:require [hiccup.form :refer [form-to label submit-button]]
            [hiccup.core :refer [html]]))

(defn search-form [term]
  (form-to [:get "/contacts"]
           (label "q" "Search Term")
           [:input {:type "search" :id "search" :name "q" :value term}]
           (submit-button "Search")))

(defn contacts-handler [request]
  (println "Invoking contacts-handler")
  (clojure.pprint/pprint request)
  (let [query (get (:params request) "q")]
    (if query (str "Contact: " query)
        (html (search-form query)))))