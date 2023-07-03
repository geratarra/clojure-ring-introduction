(ns web-app-utils 
  (:require [compojure.core :refer [routes]]
            [compojure.route :refer [not-found]]
            [api.api-routes :refer [api-routes]]
            [routes :refer [app-routes]]
            [ring.middleware.json :refer [wrap-json-response]]))

(def combined-routes
  (routes app-routes
          (wrap-json-response api-routes)
          (not-found "Page not found")))