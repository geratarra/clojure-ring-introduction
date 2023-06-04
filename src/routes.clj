(ns routes
  (:require [compojure.core :refer [defroutes DELETE GET POST]]
            [compojure.route :refer [not-found]]
            [handlers]
            [ring.util.response :refer [redirect]]))

(defroutes app-routes
  (GET "/" request (redirect "/contacts"))
  (GET "/contacts" request handlers/contacts-handler)
  (GET "/contacts/new" request handlers/add-get-contact-handler)
  (GET "/contacts/:id" request handlers/contact-details-handler)
  (POST "/contacts/:id/edit" request handlers/edit-post-contact-handler)
  (GET "/contacts/:id/edit" request handlers/edit-contact-handler)
  (DELETE "/contacts/:id" request handlers/delete-contact-handler)
  (POST "/contacts/new" request handlers/add-post-contact-handler)
  (GET "/contacts/:id/email" request handlers/validate-email-handler)
  (not-found "Page not found"))