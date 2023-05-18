(ns handlers
  (:require [clojure.string :refer [blank?]]
            [hiccup.core :refer [html]]
            [hiccup.form :refer [form-to label submit-button]]))

(def contacts [{:id 1
                :first-name "Gerardo"
                :last-name "Tarragona"
                :phone "662-1-93-31-53"
                :emai "geratarra@gmail.com"}
               {:id 2
                :first-name "Foo"
                :last-name "Last-name"
                :phone "123456789"
                :emai "test@test.com"}
               {:id 3
                :first-name "Bar"
                :last-name "Last-name"
                :phone "987654321"
                :emai "test@test.com"}])

(defn get-contact [first-name contacts]
  (filter (fn [contact] (= first-name (:first-name contact))) contacts))

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
                   [:td (:emai contact)]
                   [:td [:a {:href (str "/contacts/" (:id contact) "/edit")} "Edit"]
                    [:a {:href (str "/contacts/" (:id contact))} "View"]]]) _contacts)]])

(defn contacts-page [term _contacts]
  [:div (search-form term)
   (contacts-table _contacts)
   [:p [:a {:href "/contacts/new"} "Add Contact"]]])

(defn contacts-handler [request]
  (println "Invoking contacts-handler")
  (clojure.pprint/pprint request)
  (let [query (get (:params request) "q")]
    (if (blank? query) 
      (html (contacts-page query contacts))
      (html (contacts-page query (get-contact query contacts))))))