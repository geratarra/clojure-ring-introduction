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

(defn add-contact [contact contacts]
  (swap! contacts conj (assoc contact :id (inc (:id (last @contacts))))))

(defn edit-contact [contact contacts]
  (let [index-of-contact (.indexOf @contacts (first (get-contacts :id (:id contact) @contacts)))]
    (reset! contacts
            (assoc-in (into [] @contacts)
                      [index-of-contact]
                      contact))
    contact))

(defn delete-contact [contact contacts]
  (reset! contacts (filter (fn [_contact] (not= (:id _contact) (:id contact))) @contacts)))