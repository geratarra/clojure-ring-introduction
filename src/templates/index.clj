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
           [:input {:type "search" :id "search" :name "q" :value term}]
           (submit-button "Search")))

(defn contacts-table [contacts]
  [:table
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
                    [:a {:href (str "/contacts/" (:id contact))} "View"]]]) contacts)]])

(defn pagination-controls [page contacts]
  [:div
   [:span
    (if (> page 1)
      [:a {:href (str "/contacts?page=" (- page 1))} "Previous"]
      nil)]
   [:span
    (if (= (count contacts) 10)
      [:a {:href (str "/contacts?page=" (inc page))} "Next"]
      nil)]])

(defn contacts-view [term contacts page]
  [:div (search-form term)
   (contacts-table contacts)
   (pagination-controls page contacts)
   [:p [:a {:href "/contacts/new"} "Add Contact"]]])
