(ns routes
  (:require
   [compojure.core :as compojure]
   [compojure.route :as compojure-route]
   [handlers]))

(compojure/defroutes app-routes
  (compojure/GET "/contacts" params handlers/contacts-handler)
  (compojure/GET "/" params handlers/contacts-handler)

  (compojure-route/not-found "Page not found"))