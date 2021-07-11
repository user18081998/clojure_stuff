(defn captalize [s] (clojure.string/upper-case s))

(defn reverse-str [s] (str (clojure.string/reverse s)))


(defn meditate 
  "for docs purposes heheheheh"
  [s calmness-level]
    (cond
      (< 0 calmness-level 5) (str (.toUpperCase s) ", I TELL YA")
      (<= 5 calmness-level 9) (captalize s)
      (= calmness-level 10) (reverse-str s)
      :else "invalid argument"
    )
  )
(meditate "hello" -6)