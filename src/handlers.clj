(ns handlers
  (:require [clojure.walk :refer [keywordize-keys]]
            [hiccup.core :refer [html]]
            [ring.util.response :refer [redirect]]
            [services.contacts-service :refer [add-contact
                                               calculate-end-contacts-index contacts
                                               delete-contact edit-contact get-contacts validate-email-existence]]
            [templates.add-contact :refer [add-contact-form]]
            [templates.contact-details :refer [contact-details-view]]
            [templates.edit-contact :refer [edit-contact-form]]
            [templates.index :refer [contacts-view]]))

(defn add-get-contact-handler [request]
  (html (add-contact-form {})))

(defn add-post-contact-handler [request]
  (let [contact (add-contact (keywordize-keys (:form-params request)) contacts)]
    (if (:error contact)
      (html (add-contact-form contact))
      (redirect "/contacts" 303))))

(defn contacts-handler [request]
  (let [query (get-in request [:params :q])
        page (if (empty? (get-in request [:params :page]))
               1
               (Integer/parseInt (get-in request [:params :page])))
        filtered-contacts (get-contacts :first-name query @contacts)
        contacts-split (subvec filtered-contacts (- (* page 10) 10) (calculate-end-contacts-index page filtered-contacts))]
    (html (contacts-view query contacts-split page))))

(defn contact-details-handler [request]
  (html (contact-details-view (first (filter (fn [contact]
                                               (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))

(defn edit-contact-handler [request]
  (html (edit-contact-form (first (filter (fn [contact]
                                            (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))

(defn edit-post-contact-handler [request]
  (let [contact (edit-contact (assoc (:params request) :id (Integer/parseInt (get-in request [:params :id]))) contacts)]
    (if (:error contact)
      (html (edit-contact-form contact))
      (redirect "/contacts"))))

(defn delete-contact-handler [request]
  (delete-contact (assoc (keywordize-keys (:params request)) :id (Integer/parseInt (get-in request [:params :id]))) contacts)
  (redirect "/contacts" 303))

(defn validate-email-handler [request]
  (let [contact-id (if (= nil (get-in request [:params :id]))
                     nil
                     (Integer/parseInt (get-in request [:params :id])))
        contact (validate-email-existence (assoc (:params request) :id contact-id) @contacts)
        response {}]
    (if (nil? (get-in contact [:email :error]))
      (assoc response :status 200 :body "")
      (assoc response :status 200 :body (get-in contact [:email :error])))))