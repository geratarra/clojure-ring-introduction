(ns core
  (:require [clojure.pprint :refer [pprint]]
            [hiccup.page :refer [html5 include-css include-js]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [routes :refer [app-routes]]
            [templates.index :refer [create-head create-html]]
            [tea-time.core :as tt]))

(defn wrap-index [handler head]
  (fn [request]
    (let [response (handler request)
          content-type (get-in response [:headers "Content-Type"])]
      (pprint "==========REQUEST==========")
      (pprint request)
      (pprint "==========RESPONSE==========")
      (pprint response)
      (if (and
           (= content-type "text/html; charset=utf-8")
           (or (:include-base-template response)))
        (update response :body (fn [body] (html5 (create-html head body))))
        response))))

(defn -main [& args]
  (println "Server started at port 3000")
  (tt/start!)
  (run-jetty (-> #'app-routes
                 wrap-params
                 wrap-reload
                 (wrap-defaults site-defaults)
                 (wrap-index (create-head (include-js "htmx.min.js") (include-css "main.css"))))
             {:port 3000
              :join? false}))