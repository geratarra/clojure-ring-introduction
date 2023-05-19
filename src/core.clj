(ns core
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [routes :refer [app-routes]]))


(defn -main [& args]
  (println "Server started at port 3000")
  (run-jetty (-> #'app-routes
                 wrap-params
                 wrap-reload) {:port 3000
                                      :join? false}))