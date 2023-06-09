(ns handlers
  [:require
   [clojure.data.json :as json]
   [clojure.walk :refer [keywordize-keys]]
   [hiccup.core :refer [html]]
   [ring.util.response :refer [redirect response header content-type]]
   [services.archive-service :refer [archive-atom]]
   [services.contacts-service :refer [add-contact
                                      calculate-end-contacts-index contacts
                                      delete-contact edit-contact get-contacts validate-email-existence]]
   [tea-time.core :as tt]
   [templates.add-contact :refer [add-contact-form]]
   [templates.contact-details :refer [contact-details-view]]
   [templates.edit-contact :refer [edit-contact-form]]
   [templates.index :refer [contact-rows-template contacts-view
                            download-template]]])

(defn add-get-contact-handler [request]
  (html (add-contact-form {})))

(defn add-post-contact-handler [request]
  (let [contact (add-contact (keywordize-keys (:form-params request)) contacts)]
    (if (:error contact)
      (html (add-contact-form contact))
      (redirect "/contacts" 303))))

(defn contacts-handler [request]
  (let [query (get-in request [:params :q])
        page (if (empty? (get-in request [:params :page]))
               1
               (Integer/parseInt (get-in request [:params :page])))
        filtered-contacts (get-contacts :first-name query @contacts)
        contacts-split (subvec filtered-contacts (- (* page 10) 10) (calculate-end-contacts-index page filtered-contacts))
        hx-trigger (get-in (keywordize-keys request) [:headers :hx-trigger])
        html-response (if (or (= hx-trigger "search") (= hx-trigger "load-more"))
                        {:body (html (contact-rows-template contacts-split page)) :status 200 :headers {"Content-Type" "text/html; charset=utf-8"}}
                        {:body (html (contacts-view query contacts-split page @archive-atom)) :status 200 :headers {"Content-Type" "text/html; charset=utf-8"} :include-base-template true})] 
    html-response))

(defn contact-details-handler [request]
  (html (contact-details-view (first (filter (fn [contact]
                                               (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))

(defn edit-contact-handler [request]
  (html (edit-contact-form (first (filter (fn [contact]
                                            (= (:id contact) (Integer/parseInt (get-in request [:params :id])))) @contacts)))))

(defn edit-post-contact-handler [request]
  (let [contact (edit-contact (assoc (:params request) :id (Integer/parseInt (get-in request [:params :id]))) contacts)]
    (if (:error contact)
      (html (edit-contact-form contact))
      (redirect "/contacts"))))

(defn delete-contact-handler [request]
  (let [hx-trigger (get-in (keywordize-keys request) [:headers :hx-trigger])]
    (delete-contact (assoc (keywordize-keys (:params request)) :id (Integer/parseInt (get-in request [:params :id]))) contacts)
    (if (= hx-trigger "delete-btn")
      (redirect "/contacts" 303)
      "")))

(defn delete-contacts-handler [request] 
  (let [contact-ids (if (nil? (get-in request [:params :selected_contact_ids]))
                      []
                      (-> (get-in request [:params :selected_contact_ids])
                          vector
                          flatten))
        page (if (not-empty (get-in request [:params :page_input]))
               (Integer/parseInt (get-in request [:params :page_input]))
               1)
        updated-contacts (doall (map (fn [id] (delete-contact {:id (Integer/parseInt id)} contacts)) contact-ids))
        contacts-split (subvec (vec @contacts) (- (* page 10) 10) (calculate-end-contacts-index page @contacts))]
    (html (contacts-view nil contacts-split page @archive-atom))))

(defn validate-email-handler [request]
  (let [contact-id (if (= nil (get-in request [:params :id]))
                     nil
                     (Integer/parseInt (get-in request [:params :id])))
        contact (validate-email-existence (assoc (:params request) :id contact-id) @contacts)
        response {}]
    (if (nil? (get-in contact [:email :error]))
      (assoc response :status 200 :body "")
      (assoc response :status 200 :body (get-in contact [:email :error])))))

(defn contacts-count-handler [request]
  (str "(" (count @contacts) " total Contacts)"))

(defn contacts-archive-handler [request]
  (swap! archive-atom assoc :progress 0.1)
  (swap! archive-atom assoc
         :task
         (tt/every! 1 (bound-fn [] (swap! archive-atom assoc :progress (+ 0.1 (:progress @archive-atom)))))
         :status "running")
  (html (download-template @archive-atom)))

(defn archive-file-handler [request]
  (-> (response (json/json-str @contacts))
      (content-type "application/json")
      (header "Content-Disposition" "attachment; filename=\"contacts.json\"")))

(defn archive-status-handler [request]
  (when (> (:progress @archive-atom) 1.0)
    (swap! archive-atom assoc :progress nil)
    (swap! archive-atom assoc :status "complete")
    (tt/cancel! (:task @archive-atom)))
  (html (download-template @archive-atom)))

(defn clear-archive-handler [request]
  (swap! archive-atom assoc :status "waiting")
  (html (download-template @archive-atom)))