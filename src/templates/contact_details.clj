(ns templates.contact-details)

(defn contact-details-view [contact]
  [:div
   [:h1 (str (:first-name contact) " " (:last-name contact))]
   [:div
    [:div (str "Phone: " (:phone contact))]
    [:div (str "Email: " (:email contact))]]
   [:p
    [:a {:href (str "/contacts/" (:id contact) "/edit")} "Edit"]
    [:span "&nbsp;"]
    [:a {:href "/contacts"} "Back"]]])