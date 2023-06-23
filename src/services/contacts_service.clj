(ns services.contacts-service 
  (:require [clojure.string :refer [blank?]]))

(def contacts (atom [{:id 1 :first-name "Foo" :last-name "Bar" :phone "123456789" :email "test@test.com"}
                     {:id 2 :first-name "Bee" :last-name "Lol" :phone "123456789" :email "bee@test.com"}
                     {:id 3 :first-name "Lorum" :last-name "Ipsum" :phone "987654321" :email "lorump@test.com"}
                     {:id 4, :first-name "name4", :last-name "lastname4", :phone "12345-4", :email "test4@test.com"}
                     {:id 5, :first-name "name5", :last-name "lastname5", :phone "12345-5", :email "test5@test.com"}
                     {:id 6, :first-name "name6", :last-name "lastname6", :phone "12345-6", :email "test6@test.com"}
                     {:id 7, :first-name "name7", :last-name "lastname7", :phone "12345-7", :email "test7@test.com"}
                     {:id 8, :first-name "name8", :last-name "lastname8", :phone "12345-8", :email "test8@test.com"}
                     {:id 9, :first-name "name9", :last-name "lastname9", :phone "12345-9", :email "test9@test.com"}
                     {:id 10, :first-name "name10", :last-name "lastname10", :phone "12345-10", :email "test10@test.com"}
                     {:id 11, :first-name "name11", :last-name "lastname11", :phone "12345-11", :email "test11@test.com"}
                     {:id 12, :first-name "name12", :last-name "lastname12", :phone "12345-12", :email "test12@test.com"}
                     {:id 13, :first-name "name13", :last-name "lastname13", :phone "12345-13", :email "test13@test.com"}
                     {:id 14, :first-name "name14", :last-name "lastname14", :phone "12345-14", :email "test14@test.com"}
                     {:id 15, :first-name "name15", :last-name "lastname15", :phone "12345-15", :email "test15@test.com"}
                     {:id 16, :first-name "name16", :last-name "lastname16", :phone "12345-16", :email "test16@test.com"}
                     {:id 17, :first-name "name17", :last-name "lastname17", :phone "12345-17", :email "test17@test.com"}
                     {:id 18, :first-name "name18", :last-name "lastname18", :phone "12345-18", :email "test18@test.com"}
                     {:id 19, :first-name "name19", :last-name "lastname19", :phone "12345-19", :email "test19@test.com"}
                     {:id 20, :first-name "name20", :last-name "lastname20", :phone "12345-20", :email "test20@test.com"}
                     {:id 21, :first-name "name21", :last-name "lastname21", :phone "12345-21", :email "test21@test.com"}
                     {:id 22, :first-name "name22", :last-name "lastname22", :phone "12345-22", :email "test22@test.com"}
                     {:id 23, :first-name "name23", :last-name "lastname23", :phone "12345-23", :email "test23@test.com"}
                     {:id 24, :first-name "name24", :last-name "lastname24", :phone "12345-24", :email "test24@test.com"}
                     {:id 25, :first-name "name25", :last-name "lastname25", :phone "12345-25", :email "test25@test.com"}
                     {:id 26, :first-name "name26", :last-name "lastname26", :phone "12345-26", :email "test26@test.com"}
                     {:id 27, :first-name "name27", :last-name "lastname27", :phone "12345-27", :email "test27@test.com"}
                     {:id 28, :first-name "name28", :last-name "lastname28", :phone "12345-28", :email "test28@test.com"}
                     {:id 29, :first-name "name29", :last-name "lastname29", :phone "12345-29", :email "test29@test.com"}
                     {:id 30, :first-name "name30", :last-name "lastname30", :phone "12345-30", :email "test30@test.com"}
                     {:id 31, :first-name "name31", :last-name "lastname31", :phone "12345-31", :email "test31@test.com"}
                     {:id 32, :first-name "name32", :last-name "lastname32", :phone "12345-32", :email "test32@test.com"}
                     {:id 33, :first-name "name33", :last-name "lastname33", :phone "12345-33", :email "test33@test.com"}
                     {:id 34, :first-name "name34", :last-name "lastname34", :phone "12345-34", :email "test34@test.com"}
                     {:id 35, :first-name "name35", :last-name "lastname35", :phone "12345-35", :email "test35@test.com"}
                     {:id 36, :first-name "name36", :last-name "lastname36", :phone "12345-36", :email "test36@test.com"}
                     {:id 37, :first-name "name37", :last-name "lastname37", :phone "12345-37", :email "test37@test.com"}
                     {:id 38, :first-name "name38", :last-name "lastname38", :phone "12345-38", :email "test38@test.com"}
                     {:id 39, :first-name "name39", :last-name "lastname39", :phone "12345-39", :email "test39@test.com"}
                     {:id 40, :first-name "name40", :last-name "lastname40", :phone "12345-40", :email "test40@test.com"}
                     {:id 41, :first-name "name41", :last-name "lastname41", :phone "12345-41", :email "test41@test.com"}
                     {:id 42, :first-name "name42", :last-name "lastname42", :phone "12345-42", :email "test42@test.com"}
                     {:id 43, :first-name "name43", :last-name "lastname43", :phone "12345-43", :email "test43@test.com"}
                     {:id 44, :first-name "name44", :last-name "lastname44", :phone "12345-44", :email "test44@test.com"}
                     {:id 45, :first-name "name45", :last-name "lastname45", :phone "12345-45", :email "test45@test.com"}]))

(defn get-contacts [_key value contacts]
  (if (or (number? value) (not (blank? value)))
    (into [] (filter (fn [contact] (= value (_key contact))) contacts))
    (into [] contacts)))

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
  (reset! contacts (filterv (fn [_contact] (not= (:id _contact) (:id contact))) @contacts)))

(defn calculate-end-contacts-index [page contacts]
  (if (> (* page 10) (count contacts))
    (count contacts)
    (* page 10)))
