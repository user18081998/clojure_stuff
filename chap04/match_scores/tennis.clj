(ns packt-clj.tennis
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [semantic-csv.core :as sc]))

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