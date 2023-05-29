(ns core
  (:require
   [hiccup.page :refer [html5 include-js]]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.reload :refer [wrap-reload]]
   [routes :refer [app-routes]]
   [templates.index :refer [create-head create-html]]))

(defn wrap-index [handler head]
  (fn [request]
    (let [response (handler request)
          content-type (get-in response [:headers "Content-Type"])]
      (if (= content-type "text/html; charset=utf-8")
        (update response :body (fn [body] (html5 (create-html head body))))
        response))))

(defn -main [& args]
  (println "Server started at port 3000")
  (run-jetty (-> #'app-routes
                 wrap-params
                 wrap-reload
                 (wrap-defaults site-defaults)
                 (wrap-index (create-head (include-js "htmx.min.js"))))
             {:port 3000
              :join? false}))