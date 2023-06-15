(ns templates.index
  (:require [hiccup.form :refer [form-to label submit-button]]
            [hiccup.page :refer [html5]]))

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
                    :hx-select "tbody tr"}]
           (submit-button "Search")))

(defn contacts-table [contacts page]
  [:table {:style "border-collapse: separate; border-spacing: 0em 6em;"}
   [:thead [:tr [:th "First"] [:th "Last"] [:th "Phone"] [:th "Email"]]]
   [:tbody (map (fn [contact]
                  [:tr
                   [:td (:first-name contact)]
                   [:td (:last-name contact)]
                   [:td (:phone contact)]
                   [:td (:email contact)]
                   [:td
                    [:a {:href (str "/contacts/" (:id contact) "/edit")} "Edit"]
                    [:span "&nbsp;"]
                    [:a {:href (str "/contacts/" (:id contact))} "View"]]]) contacts)
    (if (= 10 (count contacts))
      [:tr [:td {:colspan "5" :style "text-align: center;"}
            [:span {:hx-target "closest tr"
                    :hx-swap "outerHTML"
                    :hx-trigger "revealed"
                    :hx-select "tbody > tr"
                    :hx-push-url "true"
                    :hx-get (str "/contacts?page=" (+ page 1))} "Load more"]]]
      nil)]])

(defn contacts-view [term contacts page]
  [:div (search-form term)
   (contacts-table contacts page)
   [:p [:a {:href "/contacts/new"} "Add Contact"]]])
