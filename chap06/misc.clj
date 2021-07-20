; doseq is just a foreach
(doseq [n (range 5)] (println n))
(doseq [n (filter odd? (range 5))] (println n))

; repeat returns a lazy infinite sequence of its arg
(zipmap [:score :hits :friends :level :energy :boost] (repeat 0))

; repeatedly zorks like repat but instead takes a function
; repeatedly doesnt provide any args to its func
(take 10 (repeatedly (partial rand-int 100)))
(repeatedly 10 (partial rand-int 100)) ; this is equivalent to the previous line


; iterate takes funct and arguments, 
; the result of each call is passed on to the next iteration
(defn savings [principal yearly-rate]
  (let [monthly-rate (+ 1 (/ yearly-rate 12))]
    (iterate (fn [p] (* p monthly-rate)) principal)))
(take 13 (savings 1000 0.01))
(savings 1000 0.01)


; an endless stream of groceries

(defn recur-tree-sum [acc x]
  (cond (not x) acc
        (integer? (first x))
        (recur (+ acc (first x)) (next x))
        :else (recur (recur-tree-sum acc (first x)) (next x))))

(recur-tree-sum 0 [5 12 [3 48 16] [1 [53 8 [[4 43]] [8 19 3]] 29]])

