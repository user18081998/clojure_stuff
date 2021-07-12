; destructuring

; 2. destructuring associatives

(def mapjet-booking
  {:id 8773
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sundays"
   :flights [{:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}
              :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}}
             {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}
              :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}]})

(let
 [{:keys [customer-name flights]} mapjet-booking]
  (println "customer '" customer-name "' has" (count flights) "flights."))
; using [{:keys [& ks ]} map]


;; lambdas partial comp apply 

(def weapon-fn-map
  {:fists (fn [health] (if (< health 100) (- health 10) health))
   :staff (partial + 35)
   :sword #(- % 100)
   :cast-iron-saucepan #(- % 100 (rand-int 50))
   :sweet-potato identity})

(defn strike
  "With one argument, strike a target with a default :fists `weapon`. With two
   argument, strike a target with `weapon` and return the target entity"
  ([target] (strike target :fists))
  ([target weapon] (let [weapon-fn (weapon weapon-fn-map)]
                     (update target :health weapon-fn))))

(def enemy {:name "Arnold", :health 250})

(strike enemy :sword)

(update enemy :health (comp (:sword weapon-fn-map) (:cast-iron-saucepan weapon-fn-map)))


(defn mighty-strike
  [target]
  (let [weapon-fns (vals weapon-fn-map)
        weapon-ult-fn (apply comp weapon-fns)]
    (update target :health weapon-ult-fn)))
(mighty-strike enemy)


; defining strike with multimethods


;(defmulti fn-name dispatch-funct)
; (defmulti strike (fn [m] (get m :weapon)))
; which can also be written as (sinec keys are functions of maps)
(ns-unmap 'user 'strike)
(defmulti strike :weapon)

(defmethod strike :sword
  [{{:keys [:health]} :target}]
  (- health 100))

(defmethod strike :cast-iron-saucepan
  [{{:keys [:health]} :target}]
  (- health 100 (rand-int 50)))

(strike {:weapon :sword :target {:health 200}})