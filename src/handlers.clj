(ns handlers
  (:require [clojure.string :refer [blank?]]
            [clojure.walk :refer [keywordize-keys]]
            [hiccup.core :refer [html]]
            [ring.util.response :refer [redirect]]
            [services.contacts-service :refer [add-contact contacts
                                               get-contacts edit-contact delete-contact]]
            [templates.add-contact :refer [add-contact-form]]
            [templates.contact-details :refer [contact-details-view]]
            [templates.edit-contact :refer [edit-contact-form]]
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
      (html (contacts-view query (get-contacts query :first-name @contacts))))))

(defn contact-details-handler [request]
  (clojure.pprint/pprint request)
  (html (contact-details-view (first (filter (fn [contact]
                                               (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))

(defn edit-contact-handler [request]
  (html (edit-contact-form (first (filter (fn [contact]
                                            (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))

(defn edit-post-contact-handler [request] 
  (let [contact (edit-contact (assoc (keywordize-keys (:params request)) :id (Integer/parseInt (get-in request [:params :id]))) contacts)]
    (if (:error contact)
      (html (edit-contact-form contact))
      (redirect "/contacts"))))

(defn delete-contact-handler [request]
  (delete-contact (assoc (keywordize-keys (:params request)) :id (Integer/parseInt (get-in request [:params :id]))) contacts)
  (redirect "/contacts"))