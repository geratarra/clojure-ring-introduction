(ns templates.index
  (:require [hiccup.form :refer [form-to label submit-button]]))

(defn search-form [term]
  (form-to [:get "/contacts"]
           (label "q" "Search Term")
           [:input {:type "search" :id "search" :name "q" :value term}]
           (submit-button "Search")))

(defn contacts-table [_contacts]
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
                    [:a {:href (str "/contacts/" (:id contact))} "View"]]]) _contacts)]])

(defn contacts-view [term _contacts]
  [:div (search-form term)
   (contacts-table _contacts)
   [:p [:a {:href "/contacts/new"} "Add Contact"]]])