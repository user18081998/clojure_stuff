; reduce

(reduce + [1 2 3])


(def weather-days
  [{:max 31
    :min 27
    :description :sunny
    :date "2019-09-24"}
   {:max 28
    :min 25
    :description :cloudy
    :date "2019-09-25"}
   {:max 22
    :min 18
    :description :rainy
    :date "2019-09-26"}
   {:max 23
    :min 16
    :description :stormy
    :date "2019-09-27"}
   {:max 35
    :min 19
    :description :sunny
    :date "2019-09-28"}])

; find highest temp

; with map
(apply max (map :max weather-days))

; with reduce
(reduce
 (fn [day1 day2] (if (> (:max day1) (:max day2)) day1 day2))
 weather-days)

; find max and min in a seq with reduce
(defn max-min [col]
  (reduce
   (fn [{:keys [minimum maximum]} new-number]
     {:minimum (if (and minimum (> new-number minimum)) minimum new-number)
      :maximum (if (and maximum (< new-number maximum)) maximum new-number)})
   {}
   col))

(max-min [1 2 3 4 5])

; generate random sequence with reduce

(defn random-seq [n]
  (reduce (fn [acc,col] (conj acc (rand-int 10000))) [] (range n)))

(random-seq 6)

(max-min (random-seq 100))


; partitioning
(def s (random-seq 9))
s
(partition 2 s)
(partition-all 2 s)
(partition-by #(> % 5000) (sort s))

; partitioning with reduce
(defn partition-custom
  "apply a partitioning scheme with f reduction until predicate holds on accumulator "
  [col f p]
  (reduce
   (fn [{:keys [segments current] :as acc} n]
     (let [current-with-n (conj current n)
           total-with-n (apply f current-with-n)]
       (if (p total-with-n)
         (assoc acc
                :segments (conj segments current)
                :current [n])
         (assoc acc :current current-with-n))))
   {:segments [] :current []}
   col))

(partition-custom (sort s) + #(> % 20))

(partition-custom [4 19 4 9 5 12 5 3 4 1 1 9 5 18] + #(> % 20))


; elevation differences on slopes

(def distance-elevation
  [[0 400]
   [12.5 457]
   [19 622]
   [21.5 592]
   [29 615]
   [35.5 892]
   [39 1083]
   [43 1477]
   [48.5 1151]
   [52.5 999]
   [57.5 800]
   [62.5 730]
   [65 1045]
   [68.5 1390]
   [70.5 1433]
   [75 1211]
   [78.5 917]
   [82.5 744]
   [84 667]
   [88.5 860]
   [96 671]
   [99 584]
   [108 402]
   [115.5 473]])


; shape of data : 
; list of tuples
; for each tuple
;   the first value is the distance from the start of the race
;   the second is the elevation at that point

(defn distances-elevation-to-next-peak-or-valley
  [data]
  (->
   (reduce
    (fn [{:keys [current] :as acc} [distance elevation :as this-position]])
    {:current []
     :calculated []}
    (reverse data))
   :calculated
   reverse))



















