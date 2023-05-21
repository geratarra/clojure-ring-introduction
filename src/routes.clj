(ns routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [handlers]
            [ring.util.response :refer [redirect]]))

(defroutes app-routes
  (GET "/" request (redirect "/contacts"))
  (GET "/contacts" request handlers/contacts-handler)
  (GET "/contacts/new" request handlers/add-get-contact-handler)
  (GET "/contacts/:id" request handlers/contact-details)
  (POST "/contacts/new" request handlers/add-post-contact-handler)
  (not-found "Page not found"))