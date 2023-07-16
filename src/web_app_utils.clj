(ns web-app-utils 
  (:require [api.api-routes :refer [api-routes]]
            [compojure.core :refer [GET routes]]
            [compojure.route :refer [not-found]]
            [ring.middleware.json :refer [wrap-json-response]]
            [routes :refer [app-routes]]))

(def listening-port 8080)

(def combined-routes
  (routes
   app-routes
   (wrap-json-response api-routes)
   (not-found "Page not found")))