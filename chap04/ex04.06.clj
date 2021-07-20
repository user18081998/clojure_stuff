(def game-users
  [{:id 9342
    :username "speedy"
    :current-points 45
    :remaining-lives 2
    :experience-level 5
    :status :active}
   {:id 9854
    :username "stealthy"
    :current-points 1201
    :remaining-lives 1
    :experience-level 8
    :status :speed-boost}
   {:id 3014
    :username "sneaky"
    :current-points 725
    :remaining-lives 7
    :experience-level 3
    :status :active}
   {:id 2051
    :username "forgetful"
    :current-points 89
    :remaining-lives 4
    :experience-level 5
    :status :imprisoned}
   {:id 1032
    :username "wandering"
    :current-points 2043
    :remaining-lives 12
    :experience-level 7
    :status :speed-boost}
   {:id 7213
    :username "slowish"
    :current-points 143
    :remaining-lives 0
    :experience-level 1
    :status :speed-boost}
   {:id 5633
    :username "smarter"
    :current-points 99
    :remaining-lives 4
    :experience-level 4
    :status :terminated}
   {:id 3954
    :username "crafty"
    :current-points 21
    :remaining-lives 2
    :experience-level 8
    :status :active}
   {:id 7213
    :username "smarty"
    :current-points 290
    :remaining-lives 5
    :experience-level 12
    :status :terminated}
   {:id 3002
    :username "clever"
    :current-points 681
    :remaining-lives 1
    :experience-level 8
    :status :active}])


; different ways to write a map
(map #(:current-points %) game-users)
(map (fn [player] (:current-points player)) game-users)
(map :current-points game-users)

; threading a filter and a map, using a set as a function
(->> game-users
     (filter (comp #{:active :imprisoned :speed-boost} :status))
     (map :current-points))

; mapcat
(def alpha-lc ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j"])
(mapcat #(conj '() % (clojure.string/upper-case %)) alpha-lc)

; map with multiple args requires mapping function to have that many args
(map #(+ % %2) [1 2 3] [1 2 3])

; into merges sequences
(doc into)
(into [1 2 3] [2 4 6])

; zipmap returns a map with %1 as keys and %2 as vals
(doc zipmap)
(zipmap [:1 :2 :3] [1 2 3]) ; => {:1 1, :2 2, :3 3}

; zipmap can we rewritten as
(defn zipmap-custom [x y] (into {} (map #(conj [] %1 %2) x y)))
(zipmap-custom [:1 :2 :3] [1 2 3])

(def meals ["breakfast" "lunch" "dinner" "midnight snack"])
(zipmap (range) meals)
(into {} (map #(assoc {} %1 %2) (range) meals))
(->> meals
     (map #(assoc {} %1 %2) (range))
     (into {}))

(doc map-indexed)
(->> meals
     (map-indexed #(assoc {} %1 %2))
     (into {}))

; sum on list of window 2
(map + (range 10) (rest (range 10)))

(def temperature-by-day
  [18 23 24 23 27 24 22 21 21 20 32 33 30 29 35 28 25 24 28 29 30])

(map (fn [today yday]
       (cond (< today yday) :colder
             (< yday today) :warmer
             (= yday today) :unchanged))
     (rest temperature-by-day)
     temperature-by-day)

; max cant work on a list but it accepts an unlimited number of args
; use to apply to turn elements of a sequence into args 
(apply max temperature-by-day)
(apply min temperature-by-day)

(defn f-value-by-status[field status users f]
  (->> users
       (filter #(= (:status %) status))
       (map field)
       (apply f)))

(defn max-value-by-status [field status users] 
  (f-value-by-status field status users max))

(defn min-value-by-status [field status users]
  (f-value-by-status field status users min))

(max-value-by-status :experience-level :terminated game-users)

(require '[clojure.data.csv :as csv])