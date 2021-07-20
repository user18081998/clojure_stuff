(ns packt-clj.tennis
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [semantic-csv.core :as sc]
   [clojure.math.numeric-tower :as math]))

(def filename "match_scores_1991-2016_unindexed_csv.csv")

(defn first-match [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         first)))
(first-match "match_scores_1991-2016_unindexed_csv.csv")

(defn n-matches [csv n]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (map #(select-keys % [:tourney_year_id 
                               :winner_name 
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won]))
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/->int})
         (take n)
         doall)))

(n-matches filename 5)



;; list all tennis matches won by roger federer

(defn match-query [csv f]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/->int
                        :winner_games_won sc/->int
                        :loser_games_won sc/->int})
         (filter f)
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))

(defn federer-wins [csv] (match-query csv #(= "Roger Federer" (:winner_name %))))

(take 5 (federer-wins filename))

(defn federer-nadal-matches [csv]
  (match-query csv 
               #(= (hash-set (:winner_name %) (:loser_name %))
                   #{"Roger Federer" "Rafael Nadal"})))

(count (federer-nadal-matches filename))


(defn tennis-rivalry [csv player1 player2] 
  (match-query csv 
               #(= (hash-set (:winner_name %) (:loser_name %))
                     (hash-set player1 player2))))
(tennis-rivalry filename "Roger Federer" "Rafael Nadal")


(defn match-probability [R1 R2] (/ 1 (+ 1 (math/expt 10 (/ (- R2 R1) 400)))))

(def k-factor 32)

(defn recalculate-rating [k previous-rating expected-outcome real-outcome]
  (+ previous-rating (* k (- real-outcome expected-outcome))))

(defn elo-alg [path-to-csv k]
  (with-open [r (io/reader path-to-csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/->int
                        :winner_games_won sc/->int
                        :loser_games_won sc/->int})
         (reduce
          (fn [{:keys [players] :as acc} {:keys [:winner_name :winner_slug
                                                 :loser_name :loser_slug] :as match}]
            (let [winner-rating (get players winner_slug 400)
                  loser-rating (get players loser_slug 400)
                  winner-probability (match-probability winner-rating loser-rating)
                  loser-probability (- 1 winner-probability)
                  predictable-match? (not= winner-rating loser-rating)
                  prediction-correct? (> winner-rating loser-rating)
                  correct-predictions (if (and predictable-match? prediction-correct?)
                                        (inc (:correct-predictions acc))
                                        (:correct-predictions acc))
                  predictable-matches (if predictable-match?
                                        (inc (:predictable-match-count acc))
                                        (:predictable-match-count acc))]
              (-> acc
                  (assoc :predictable-match-count predictable-matches)
                  (assoc :correct-predictions correct-predictions)
                  (assoc-in [:players winner_slug] (recalculate-rating k winner-rating winner-probability 1))
                  (assoc-in [:players loser_slug] (recalculate-rating k loser-rating loser-probability 0))
                  (update :match-count inc))))
          {:players {}
           :match-count 0
           :predictable-match-count 0
           :correct-predictions 0}))))


(elo-alg filename k-factor)

