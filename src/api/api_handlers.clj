(ns api.api-handlers 
  (:require [ring.util.response :refer [bad-request response]]
            [services.contacts-service :as contacts-service :refer [add-contact contacts
                                                                    delete-contact edit-contact get-contacts]]))

(defn contacts-handler [request]
  (response {:contacts contacts}))

(defn add-post-contact-handler [request]
  (let [new-contact (add-contact (:params request) contacts-service/contacts)]
    (if (:error new-contact)
      (bad-request new-contact)
      (response new-contact))))

(defn contact-details-handler [request]
  (response (get-contacts :id  (Integer/parseInt (get-in request [:params :id])) @contacts)))

(defn update-contact-handler [request]
  (let [contact (edit-contact (assoc (:params request) :id (Integer/parseInt (get-in request [:params :id]))) contacts)]
    (if (:error contact)
      (bad-request contact)
      (response contact))))

(defn delete-contact-handler [request]
  (delete-contact (assoc (:params request) :id (Integer/parseInt (get-in request [:params :id]))) contacts)
  (response {:success true}))