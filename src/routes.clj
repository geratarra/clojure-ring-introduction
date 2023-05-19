(ns routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [handlers]
            [ring.util.response :refer [redirect]]))

(defroutes app-routes
  (GET "/contacts" params handlers/contacts-handler)
  (GET "/contacts/new" params handlers/add-get-contact-handler)
  (POST "/contacts/new" params handlers/add-post-contact-handler)
  (GET "/" params (redirect "/contacts"))

  (not-found "Page not found"))