(ns services.contacts-service)

(def contacts (atom [{:id 1
                      :first-name "Foo"
                      :last-name "Bar"
                      :phone "123456789"
                      :email "test@test.com"}
                     {:id 2
                      :first-name "Bee"
                      :last-name "Lol"
                      :phone "123456789"
                      :email "bee@test.com"}
                     {:id 3
                      :first-name "Lorum"
                      :last-name "Ipsum"
                      :phone "987654321"
                      :email "lorump@test.com"}]))

(defn get-contacts [_key value contacts]
  (filter (fn [contact] (= value (_key contact))) contacts))

(defn validate-email-existence [contact contacts]
  (let [current-contact (first (get-contacts :email (:email contact) contacts))]
    (if (and (not-empty current-contact)
             (not= (:id contact) (:id current-contact)))
      (assoc contact :error true :email {:error "Email already exists."})
      contact)))

(defn add-contact [contact contacts]
  (let [validated-contact (-> contact (validate-email-existence @contacts))]
    (if (:error validated-contact)
      validated-contact
      (swap! contacts conj (assoc contact :id (if (not-empty @contacts) (inc (:id (last @contacts))) 1))))))

(defn edit-contact [contact contacts]
  (let [index-of-contact (.indexOf @contacts (first (get-contacts :id (:id contact) @contacts)))
        validated-contact (-> contact (validate-email-existence @contacts))]
    (if (:error validated-contact)
      validated-contact
      (do
        (reset! contacts
                (assoc-in (into [] @contacts)
                          [index-of-contact]
                          contact))
        contact))))

(defn delete-contact [contact contacts]
  (reset! contacts (filter (fn [_contact] (not= (:id _contact) (:id contact))) @contacts)))