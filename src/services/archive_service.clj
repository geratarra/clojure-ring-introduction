(ns services.archive-service
  (:require [tea-time.core :as tt]))

(def archive-atom (atom {:status "waiting"}))

(defn reset-archive-atom [archive]
  (swap! archive assoc :progress nil)
  (swap! archive assoc :status "waiting")
  (tt/cancel! (:task @archive)))
