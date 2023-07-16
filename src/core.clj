(ns core
  (:require
   [web-app-utils :refer [combined-routes listening-port]]
   [hiccup.page :refer [html5 include-css include-js]]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.anti-forgery :as anti-forgery]
   [ring.middleware.conditional :refer [if-url-doesnt-match]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.stacktrace :refer [wrap-stacktrace]]
   [ring.logger :as logger]
   [tea-time.core :as tt]
   [templates.index :refer [create-head create-html]])
  (:gen-class))

(defn wrap-index [handler head]
  (fn [request]
    (let [response (handler request)
          content-type (get-in response [:headers "Content-Type"])]
      (if (and
           (= content-type "text/html; charset=utf-8")
           (true? (:include-base-template response)))
        (update response :body (fn [body] (html5 (create-html head body))))
        response))))

(defn -main [& args]
  (println (str "Server started at port " listening-port))
  (tt/start!)
  (run-jetty (-> combined-routes
                 wrap-params
                 (if-url-doesnt-match #".*/api/.+" anti-forgery/wrap-anti-forgery)
                 (wrap-index (create-head (include-js "htmx.min.js") (include-css "main.css")))
                 (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
                 wrap-stacktrace
                 logger/wrap-with-logger)
             {:port listening-port
              :host "0.0.0.0"
              :join? false}))