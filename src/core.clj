(ns core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [routes]))


(defn -main [& args]
  (println "Server started at port 3000")
  (run-jetty (-> #'routes/app-routes
                 wrap-params
                 wrap-reload) {:port 3000
                                      :join? false}))