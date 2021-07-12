(def player {:name "Lea" :health 200 :position {:x 10 :y 10 :facing :north}})

; (defmulti move #(:facing (:position %))) 
; dispatch function p much is #(get-in % [:position :facing])
; or also (comp :facing :position)

(#(:facing (:position %)) player)
(#(get-in % [:position :facing]) player)
((comp :facing :position) player)

(ns-unmap 'user 'move)
(defmulti move (comp :facing :position))

(defmethod move :north [entity] (update-in entity [:position :y] inc))
(defmethod move :south [entity] (update-in entity [:position :y] dec))
(defmethod move :west  [entity] (update-in entity [:position :x] inc))
(defmethod move :east  [entity] (update-in entity [:position :x] dec))

(move player)
(move {:position {:x 10 :y 10 :facing :west}})
(move {:position {:x 10 :y 10 :facing :south}})
(move {:position {:x 10 :y 10 :facing :east}})

; add default case for unknown dispatch values
(defmethod move :default [entity] entity)

(move {:position {:x 10 :y 10 :facing :ifacenothingiamvoid}})



