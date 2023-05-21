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

(defn get-contact [first-name contacts]
  (filter (fn [contact] (= first-name (:first-name contact))) contacts))

(defn add-contact [contact contacts]
  (swap! contacts conj (assoc contact :id (inc (:id (last @contacts))))))