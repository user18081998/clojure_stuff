(require '[clojure.data.csv :as csv])

(require '[clojure.java.io :as io])

(def filename "match_scores_1991-2016_unindexed_csv.csv")

(with-open [r (io/reader filename)]
  (first (csv/read-csv r)))

(with-open [r (io/reader filename)]
  (count (csv/read-csv r)))

; extract name of the winner of each of the first five matches
(with-open [r (io/reader filename)]
  (->> (csv/read-csv r)
       (map #(nth % 7))
       (take 6)
       doall))
; rem mapv returns a vector instead of a lazyseq but its prefereble 
; to use map then doall to force evaluation 


