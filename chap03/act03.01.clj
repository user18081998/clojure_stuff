; building a distance and cost calculator

(def walking-speed 5)
(def driving-speed 70)

(def paris {:lat 48.856483 :lon 2.352413})
(def bordeaux {:lat 44.834999 :lon -0.575490})

(defn distance [{lat1 :lat lon1 :lon}{lat2 :lat lon2 :lon}] 
  (let [square #(* % %)
        coef1 (square (- lat2 lat1))
        coef2 (square (- lon2 lon1))
        coef2c (* (Math/cos lat1) coef2)]
    (* 110.25 (Math/sqrt (+ coef1 coef2c)))
  ))


(distance paris bordeaux)


(defmulti itinerary :transport)

(defmethod itinerary :walking  [{from :from to :to}]
  (let [distance (distance from to),
        cost 0,
        duration (/ distance walking-speed)]
    (assoc (assoc (assoc {} :distance distance) :cost cost) :duration duration)))
  
(defn calc-cost [consumption costing distance] (* distance consumption costing))
(def dispatch-table-driving {
                             :sporche (partial calc-cost 0.12 1.5)
                             :tayato (partial calc-cost 0.07 1.5)
                             :sleta (partial calc-cost 0.2 0.1)
})

(defmethod itinerary :driving 
  [{from :from to :to vehicle :vehicle}]
  (let [
        distance (distance from to),
        duration (/ distance driving-speed),
        cost ((vehicle dispatch-table-driving) distance)]
    (assoc (assoc (assoc {} :distance distance) :cost cost) :duration duration)))

(itinerary {:from paris :to bordeaux :transport :walking})
(itinerary {:from paris :to bordeaux :transport :driving :vehicle :tayato})