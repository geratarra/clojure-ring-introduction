(ns handlers
  (:require [clojure.string :refer [blank?]]
            [clojure.walk :refer [keywordize-keys]]
            [hiccup.core :refer [html]]
            [ring.util.response :refer [redirect]]
            [services.contacts-service :refer [add-contact contacts
                                               get-contact]]
            [templates.add-contact :refer [add-contact-form]]
            [templates.contact-details :refer [contact-details-view]]
            [templates.index :refer [contacts-view]]))

(defn add-get-contact-handler [request]
  (clojure.pprint/pprint request)
  (html (add-contact-form {})))

(defn add-post-contact-handler [request]
  (clojure.pprint/pprint request)
  (let [contact (add-contact (keywordize-keys (:form-params request)) contacts)]
    (if (:error contact)
      (html (add-contact-form contact))
      (redirect "/contacts"))))

(defn contacts-handler [request]
  (let [query (get (:params request) "q")]
    (println @contacts)
    (if (blank? query)
      (html (contacts-view query @contacts))
      (html (contacts-view query (get-contact query @contacts))))))

(defn contact-details [request]
  (clojure.pprint/pprint request)
  (html (contact-details-view (first (filter (fn [contact]
                                               (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))