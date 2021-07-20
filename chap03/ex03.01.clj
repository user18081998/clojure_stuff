; destructuring
; 1. destructuring sequentials

(def booking [1425, "Bob Smith", "Allergic to unsalted peanuts only",
            [[48.9615, 2.4372], [37.742, -25.6976]],
            [[37.742, -25.6976], [48.9615, 2.4372]]])

(let [[id name _ flight1 flight2 flight3] booking] 
  (println "id :" id "\n\tname :" name "\n\tflight1 :" flight1 "\n\tflight2 :" flight2 "\n\tflight3 :" flight3))

; flight3 was bound to nil

(let [[_ name _ & flights] booking
      [[lat1 lon1] [lat2 lon2]] flights]
  (println "flights :\n\t"flights)
  (count flights)
  (println "flying from \n\tlat1 :" lat1 "lon1: " lon1
          "\nto \n\tlat2:" lat2 "lon2:" lon2))
; binding flight1 and flight2 to a sequence in [[_ name _ & flights] booking]
; then nested deconstructuring in [[[lat1 lon1] [lat2 lon2]] flights]
; using _ to skip binding

