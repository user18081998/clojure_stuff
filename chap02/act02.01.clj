(def memory-db (atom {}))

(defn read-db [] @memory-db)

(defn write-db [new-db] (reset! memory-db new-db))

(defn create-table
  [table-name]
  (let [db (read-db)]
    (write-db (assoc db table-name {:data [] :indexes {}}))))

(defn drop-table
  [table-name]
  (let [db (read-db)]
    (write-db (dissoc db table-name))))

(defn select-*
  [table]
  (let [db (read-db)
        records (get-in db [table :data])]
    records))

(defn select-*-where
  [table-name field field-value]
  (let [db (read-db)
        index (get-in db [table-name :indexes field field-value])]
    (get (get-in db [table-name :data]) index)))

(defn insert
  [table record id-key]
  (let [db (read-db)
        new-db (update-in db [table :data] conj record)
        index (- (count (get-in new-db [table :data])) 1)]
    (if (nil? (select-*-where table id-key (id-key record)))
        (write-db (assoc-in new-db [table :indexes id-key (id-key record)] index))
        (println "Record with" id-key (id-key record) "already exists. Aborting..."))))


(create-table :fruits)
(insert :fruits {:name "Pear" :stock 3} :name)
(insert :fruits {:name "Apricot" :stock 30} :name)
(insert :fruits {:name "Grapefruit" :stock 6} :name)
(select-* :fruits)
(select-*-where :fruits :name "Apricot")
(read-db)
(insert :fruits {:name "Pear" :stock 3} :name)
(drop-table :fruits)