(ns routes
  (:require [compojure.core :refer [context defroutes DELETE GET POST]]
            [compojure.route :refer [not-found]]
            [handlers]
            [ring.util.response :refer [redirect]]))

(defroutes app-routes
  (GET "/" request (redirect "/contacts"))
  (GET "/contacts" request handlers/contacts-handler)
  (GET "/contacts/count" request handlers/contacts-count-handler)
  (context "/contacts/archive" request
    (GET "/file" request handlers/archive-file-handler)
    (GET "/status" request handlers/archive-status-handler)
    (POST "/" request handlers/contacts-archive-handler)
    (DELETE "/" request handlers/clear-archive-handler))
  (GET "/contacts/new" request handlers/add-get-contact-handler)
  (POST "/contacts/new" request handlers/add-post-contact-handler)
  (GET "/contacts/email/validate" request handlers/validate-email-handler)
  (GET "/contacts/:id/email" request handlers/validate-email-handler)
  (GET "/contacts/:id" request handlers/contact-details-handler)
  (POST "/contacts/:id/edit" request handlers/edit-post-contact-handler)
  (GET "/contacts/:id/edit" request handlers/edit-contact-handler)
  (DELETE "/contacts" request handlers/delete-contacts-handler)
  (DELETE "/contacts/:id" request handlers/delete-contact-handler)
  (not-found "Page not found"))