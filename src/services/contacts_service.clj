(ns services.contacts-service)

(def contacts (atom [{:id 1
                      :first-name "Gerardo"
                      :last-name "Tarragona"
                      :phone "662-1-93-31-53"
                      :email "geratarra@gmail.com"}
                     {:id 2
                      :first-name "Foo"
                      :last-name "Last-name"
                      :phone "123456789"
                      :email "test@test.com"}
                     {:id 3
                      :first-name "Bar"
                      :last-name "Last-name"
                      :phone "987654321"
                      :email "test@test.com"}]))

(defn get-contacts [_key value contacts]
  (filter (fn [contact] (= value (_key contact))) contacts))

(defn add-contact [contact contacts]
  (swap! contacts conj (assoc contact :id (inc (:id (last @contacts))))))

(defn edit-contact [contact contacts]
  (let [index-of-contact (.indexOf @contacts (first (get-contacts :id (:id contact) @contacts)))]
    (reset! contacts
            (assoc-in @contacts
                      [index-of-contact]
                      contact))
    contact))

(defn delete-contact [contact contacts]
  (reset! contacts (filter (fn [_contact] (not= (:id _contact) (:id contact))) @contacts)))