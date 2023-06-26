(ns templates.index
  (:require [hiccup.form :refer [form-to label submit-button hidden-field]]
            [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn create-html [head body]
  (html5 [:html head [:body {:hx-boost "true"} body]]))

(defn create-head [& children]
  (let [head [:head]]
    (reduce (fn [acc child] (conj acc child)) head children)))

(defn search-form-template [term]
  (form-to {:style "margin: 1em 0em;"} [:get "/contacts"]
           (label "q" "Search Term")
           [:input {:type "search"
                    :id "search"
                    :name "q"
                    :value term
                    :hx-get "/contacts"
                    :hx-trigger "keyup delay:300ms changed, search"
                    :hx-target "tbody"
                    :hx-push-url "true"
                    :hx-indicator "#spinner"}]
           [:img {:id "spinner" :class "htmx-indicator" :src "loading.svg" :style "width: 2em;"}]
           (submit-button "Search")))

(defn contact-rows-template [contacts page]
  (let [rows (into [] (map (fn [contact]
                             [:tr
                              [:td [:input {:type "checkbox" :form "bulk-delete-form" :name "selected_contact_ids" :value (:id contact)}]]
                              [:td (:first-name contact)]
                              [:td (:last-name contact)]
                              [:td (:phone contact)]
                              [:td (:email contact)]
                              [:td
                               [:a {:href (str "/contacts/" (:id contact) "/edit")} "Edit"]
                               [:span "&nbsp;"]
                               [:a {:href (str "/contacts/" (:id contact))} "View"]
                               (form-to [:post (str "/contacts/" (:id contact) "/delete")]
                                        (anti-forgery-field)
                                        [:a {:href "#"
                                             :hx-delete (str "/contacts/" (:id contact))
                                             :hx-target "closest tr"
                                             :hx-swap "outerHTML swap:1s"
                                             :hx-confirm "Are you sure you want to delete this contact?"} "Delete Contact"])]]) contacts))
        scroll-row (when (= 10 (count contacts))
                     [:tr [:td {:colspan "5" :style "text-align: center;"}
                           [:span {:id "load-more"
                                   :hx-target "closest tr"
                                   :hx-swap "outerHTML"
                                   :hx-trigger "revealed"
                                   :hx-push-url "true"
                                   :hx-get (str "/contacts?page=" (+ page 1))} "Load more"]]])]
    (apply list (conj rows scroll-row))))

(defn contacts-table [contacts page]
  [:div
   [:table {:style "border-collapse: separate; border-spacing: 0em 6em;"}
    [:thead [:tr [:th] [:th "First"] [:th "Last"] [:th "Phone"] [:th "Email"]]]
    [:tbody (apply list (contact-rows-template contacts page))]]
   [:form {:id "bulk-delete-form"}
    (anti-forgery-field)
    (hidden-field "page_input" page)
    [:button {:hx-delete "/contacts" :hx-confirm "Are you sure you want to delete this contact?" :hx-target "body"} "Delete Selected Contacts"]]])

(defn archive-progress-template [archive]
  [:div {:hx-get "/contacts/archive/status" :hx-trigger "load delay:1000ms"}
   "Creating archive..."
   [:div {:class "progress"}
    [:div {:class "progress-bar" :id "archive-progress" :role "progressbar"
           :aria-valuenow (* (:progress archive) 100) :style (str "width: " (* (:progress archive) 100) "%;")}]]])

(defn download-template [archive]
  [:div {:id "archive-ui" :hx-target "this" :hx-swap "outerHTML"}
   (case (:status archive)
     "waiting" [:form {:id "archive-form"}
                (anti-forgery-field)
                [:button {:hx-post "/contacts/archive"} "Download Contact Archive"]]
     "running" (archive-progress-template archive)
     "complete" [:form
                 (anti-forgery-field)
                 [:a {:hx-boost "false" :href "/contacts/archive/file"} "Archive Ready!  Click here to download. &downarrow;"]
                 [:br]
                 [:button {:hx-delete "/contacts/archive"} "Clear Download"]])])

(defn contacts-view [term contacts page archive]
  [:div
   (search-form-template term)
   (download-template archive)
   (contacts-table contacts page)
   [:p [:a {:href "/contacts/new"} "Add Contact"]]
   [:span {:hx-get "/contacts/count" :hx-trigger "load"}]])