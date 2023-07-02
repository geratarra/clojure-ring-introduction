(ns api.api-routes 
  (:require [api.api-handlers :as api-handlers]
            [compojure.core :refer [defroutes DELETE GET POST PUT]]))

(defroutes api-routes
  (GET "/api/v1/contacts/:id" request api-handlers/contact-details-handler)
  (PUT "/api/v1/contacts/:id" request api-handlers/update-contact-handler)
  (DELETE "/api/v1/contacts/:id" request api-handlers/delete-contact-handler)
  (GET "/api/v1/contacts" request api-handlers/contacts-handler)
  (POST "/api/v1/contacts" request api-handlers/add-post-contact-handler))