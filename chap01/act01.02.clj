(def base-co2 382)

(def base-year 2006)

(defn co2-estimate 
  "estimate = 382 + ((Year -2006)*2)"
  [year]
  (+ 382 (* (- year 2006) 2)))

(co2-estimate 2050)


(defn co1-est2
  "try 2 for calc estimate hehehe but with let"
  [year]
  (let [year-diff (- year base-year)] (+ base-co2 (* 2 year-diff)))
)

(co1-est2 2050)