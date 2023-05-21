(ns templates.edit-contact
  (:require
   [hiccup.form :refer [form-to label text-field submit-button]]))

(defn edit-contact-form [contact]
  [:div (form-to [:post (str "/contacts/" (:id contact) "/edit")]
                 [:fieldset
                  [:legend "Contact Values"]
                  [:p (label "email" "Email")
                   (text-field {:id "email"
                                :type "email"
                                :placeholder "Email"} "email" (if (map? (:email contact))
                                                                ""
                                                                (:email contact)))
                   [:span {:class "error"} (:error (:email contact))]]
                  [:p (label "first-name" "First Name")
                   (text-field {:id "first-name"
                                :type "text"
                                :placeholder "First Name"} "first-name" (if (map? (:first-name contact))
                                                                          ""
                                                                          (:first-name contact)))
                   [:span {:class "error"} (:error (:first-name contact))]]
                  [:p (label "last-name" "Last Name")
                   (text-field {:id "last-name"
                                :type "text"
                                :placeholder "Last Name"} "last-name" (if (map? (:last-name contact))
                                                                        ""
                                                                        (:last-name contact)))
                   [:span {:class "error"} (:error (:last-name contact))]]
                  [:p (label "phone" "Phone")
                   (text-field {:id "phone"
                                :type "text"
                                :placeholder "Phone"} "phone" (if (map? (:phone contact))
                                                                ""
                                                                (:phone contact)))
                   [:span {:class "error"} (:error (:phone contact))]]
                  (submit-button "Save")])
   (form-to [:post (str "/contacts/" (:id contact) "/delete")]
            [:button "Delete Contact"])
   [:p [:a {:href "/contacts"} "Back"]]])