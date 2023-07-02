(ns api.api-handlers 
  (:require
   [ring.util.response :refer [response]]
   [services.contacts-service :as contacts-service :refer [add-contact]]))

(defn contacts-handler [request]
  (response {:contacts @contacts-service/contacts}))

(defn add-post-contact-handler [request]
  (response (add-contact (:params request) contacts-service/contacts)))