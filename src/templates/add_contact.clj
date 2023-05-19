(ns templates.add-contact
  (:require
   [hiccup.form :refer [form-to label text-field submit-button]]))

(defn add-contact-form [contact]
  [:div (form-to [:post "/contacts/new"]
                 [:legend "Contact Values"]
                 [:p (label "email" "Email")
                  (text-field {:id "email"
                               :type "email"
                               :placeholder "Email"} "email" (or (:email contact) ""))
                  [:span {:class "error"} (:email (:errors contact))]]
                 [:p (label "first-name" "First Name")
                  (text-field {:id "first-name"
                               :type "text"
                               :placeholder "First Name"} "first-name" (or (:first-name contact) ""))
                  [:span {:class "error"} (:first-name (:errors contact))]]
                 [:p (label "last-name" "Last Name")
                  (text-field {:id "last-name"
                               :type "text"
                               :placeholder "Last Name"} "last-name" (or (:last-name contact) ""))
                  [:span {:class "error"} (:last-name (:errors contact))]]
                 [:p (label "phone" "Phone")
                  (text-field {:id "phone"
                               :type "text"
                               :placeholder "Phone"} "phone" (or (:phone contact) ""))
                  [:span {:class "error"} (:phone (:errors contact))]]
                 (submit-button "Save"))
   [:p [:a {:href "/contacts"} "Back"]]])