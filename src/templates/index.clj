(ns templates.index
  (:require [hiccup.form :refer [form-to label submit-button]]
            [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn create-html [head body]
  (html5 [:html head [:body {:hx-boost "true"} body]]))

(defn create-head [& children]
  (let [head [:head]]
    (reduce (fn [acc child] (conj acc child)) head children)))

(defn search-form [term]
  (form-to [:get "/contacts"]
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

(defn contact-rows [contacts page]
  (let [rows (into [] (map (fn [contact]
                             [:tr
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
                                             :hx-target "body"
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
  [:table {:style "border-collapse: separate; border-spacing: 0em 6em;"}
   [:thead [:tr [:th "First"] [:th "Last"] [:th "Phone"] [:th "Email"]]]
   [:tbody (apply list (contact-rows contacts page))]])

(defn contacts-view [term contacts page]
  [:div
   (search-form term)
   (contacts-table contacts page)
   [:p [:a {:href "/contacts/new"} "Add Contact"]]
   [:span {:hx-get "/contacts/count" :hx-trigger "load"}]])
