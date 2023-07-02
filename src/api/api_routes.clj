(ns api.api-routes 
  (:require [api.api-handlers :as api-handlers]
            [compojure.core :refer [defroutes GET POST]]))

(defroutes api-routes
  (GET "/api/v1/contacts" request api-handlers/contacts-handler)
  (POST "/api/v1/contacts" request api-handlers/add-post-contact-handler))