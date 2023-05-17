(ns routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [handlers]
            [ring.util.response :refer [redirect]]))

(defroutes app-routes
  (GET "/contacts" params handlers/contacts-handler)
  (GET "/" params (redirect "/contacts"))

  (not-found "Page not found"))