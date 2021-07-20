(def grocery-articles [{:name "Flour"
                        :weight 1000      ; grams
                        :max-dimension 140 ; millimeters
                        }
                       {:name "Bread"
                        :weight 350
                        :max-dimension 250}
                       {:name "Potatoes"
                        :weight 2500
                        :max-dimension 340}
                       {:name "Pepper"
                        :weight 85
                        :max-dimension 90}
                       {:name "Ice cream"
                        :weight 450
                        :max-dimension 200}
                       {:name "Green beans"
                        :weight 300
                        :max-dimension 120}
                       {:name "Olive oil"
                        :weight 400
                        :max-dimension 280}])

(defn article-stream [n]
  (repeatedly n #(rand-nth grocery-articles)))

(article-stream 12)

(defn recursive-sum [acc numbers] 
  (if (first numbers) (recursive-sum (+ (first numbers) acc) (next numbers)) acc))

(recursive-sum 0 [300 25 8])

(defn full-bag? [items] 
  (let [weight (apply + (map :weight items))
        size (apply + (map :max-dimension items))]
    (or (> weight 3200) (> size 800))))

(full-bag? (article-stream 10))
(full-bag? (article-stream 1))
(full-bag? '())

(defn bag-sequences* [{:keys [current-bag bags] :as acc} stream] 
  (cond
    (not stream)
    (conj bags current-bag)
    (full-bag? (conj current-bag (first stream)))
    (recur (assoc acc
                           :current-bag [(first stream)]
                           :bags (conj bags current-bag))
                    (next stream))
    :otherwise-bag-not-full
    (recur (update acc :current-bag conj (first stream))
                    (next stream))))

(defn bag-sequences [stream] (bag-sequences* {:bags [] :current-bag []} stream))

(bag-sequences (article-stream 10000))


(defn bag-sequences-with-loop [stream]
  (loop [acc {:current-bag []
              :bags []}
         remaining-stream stream]
    (let [{:keys [current-bag bags]} acc]
      (cond
        (not remaining-stream)
        (conj bags current-bag)
        (full-bag? (conj current-bag (first remaining-stream)))
        (recur (assoc acc
                      :current-bag [(first remaining-stream)]
                      :bags (conj bags current-bag))
               (next remaining-stream))
        :otherwise-bag-not-full
        (recur (update acc :current-bag conj (first remaining-stream))
               (next remaining-stream))))))

(bag-sequences-with-loop (article-stream 10))

(clojure.repl/doc conj)


