(ns handlers
  (:require [clojure.string :refer [blank?]]
            [hiccup.core :refer [html]]
            [services.contacts-service :refer [contacts get-contact]]
            [templates.add-contact :refer [add-contact-form]]
            [templates.index :refer [contacts-page]]))

(defn add-get-contact-handler [request]
  (clojure.pprint/pprint request)
  (html (add-contact-form {})))

(defn add-post-contact-handler [request]
  (clojure.pprint/pprint request)
  (html ""))

(defn contacts-handler [request] 
  (clojure.pprint/pprint request)
  (let [query (get (:params request) "q")]
    (if (blank? query) 
      (html (contacts-page query contacts))
      (html (contacts-page query (get-contact query contacts))))))