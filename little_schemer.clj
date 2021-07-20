
(def eq? =) 
(def car #(if (list? %) (first %) nil))
(def cdr #(if (list? %) (rest %) nil))
(defn null? [a] (if (or (list? a) (nil? a)) (empty? a) nil)) 
(def atom? (comp not list?)) 

; a lat is a list of atoms

(def lat? #(cond (null? %) true 
                 (atom? (car %)) (lat? (cdr %)) 
                 :else false))
; or more clojeresquy
(def clj-lat? #(->> % 
                    list 
                    (map atom?) 
                    (every? true?)))
(defn member? [e,l] (cond 
                      (null? l) false 
                      :else (or (eq? (car l) e) (member? e (cdr l)))))
(defn rember [e,[first & rest :as l]]
  (cond (null? l) '() 
        (eq? e first) rest 
        :else (cons first (rember e rest))))
; used destructuring instead of (car l) and (cdr l)
(def firsts #(cond (null? %) '()
                   (list? (car %)) (cons (car (car %)) (firsts (cdr %)))
                   :else (firsts (cdr %))))
(defn insertR [n o l] (cond (null? l) '()
                            (eq? o (car l)) (cons o (cons n (cdr l)))
                            :else (cons (car l) (insertR n o (cdr l)))))

(defn insertL [n o l] (cond (null? l) '()
                            (eq? o (car l)) (cons n (cons o (cdr l)))
                            :else (cons (car l) (insertL n o (cdr l)))))
(defn subst [n o l](cond (null? l) '()
                            (eq? o (car l)) (cons n (cdr l))
                            :else (cons (car l) (subst n o (cdr l)))))

(defn subst2 [n o1 o2 l] (cond (null? l) '()
                               (or (eq? o1 (car l)) (eq? o2 (car l))) (cons n (cdr l))
                               :else (cons (car l) (subst2 n o1 o2 (cdr l)))))

(defn multirember [a l] (cond (null? l) '()
                              (eq? a (car l)) (multirember a (cdr l))
                              :else (cons (car l) (multirember a (cdr l)))))

(defn multiinsertR [n o l] (cond (null? l) '()
                                 (eq? o (car l)) (cons o (cons n (multiinsertR n o (cdr l))))
                                 :else (cons (car l) (multiinsertR n o (cdr l)))))
(defn multiinsertL [n o l] (cond (null? l) '()
                                 (eq? o (car l)) (cons n (cons o (multiinsertL n o (cdr l))))
                                 :else (cons (car l) (multiinsertL n o (cdr l)))))

(defn multisubst [n o l] (cond (null? l) '()
                               (eq? o (car l)) (cons n (multisubst n o (cdr l)))
                               :else (cons (car l) (multisubst n o (cdr l)))))
(def lat '(1 2 3 4 1))
(multirember 1 lat)
(multiinsertR 0 1 lat)
(multiinsertL 0 1 lat)
(multisubst 0 1 lat)


(def add1-clj (partial + 1))
(defn add1 [n] (+ 1 n))
(defn sub1 [n] (- n 1))

(defn o+ [x y] (if (zero? x) y (add1 (o+ (sub1 x) y))))
; (defun o+ (x y) (if (zero? x) y (add1 (o+ (sub1 x) y))))
(defn o- [x y] (if (zero? y) x (o- (sub1 x) (sub1 y))))
; (defun o- (x y) (if (zero? y) x (o- (sub1 x) (sub1 y))))
(o- 3 1)

(defn addtup [t] (if (null? t) 0 (o+ (car t) (addtup (cdr t)))))

(addtup lat)

(defn o* [n m] (if (zero? m) 0 (o+ n (o* n (sub1 m)))))
(eq? (o* 12 3) (o* 3 12) (* 3 12))

(defn tup+ [t1 t2] (cond
                     (and (null? t1) (null? t2)) '()
                     (null? t1) t2
                     (null? t2) t1
                     :else (cons (+ (car t1) (car t2)) (tup+ (cdr t1) (cdr t2)))))
lat
(tup+ lat (into '() (reverse (map (partial + 1) lat))))
(tup+ '(1 2 3 4 7 10) '(1))


(defn o> [n m] (cond (zero? n) false
                     (zero? m) true
                     :else (o> (sub1 n) (sub1 m))))
(defn o< [n m] (cond (zero? m) false
                     (zero? n) true
                     :else (o< (sub1 n) (sub1 m))))

(defn o=1 [n m] (cond (zero? n) (zero? m) 
                      (zero? m) false
                      :else (o=1 (sub1 n) (sub1 m))))
(o=1 50 50)

(defn o= [n m] (not (or (o< n m) (o> n m))))

(defn pow [n p] (if (zero? p) 1 (o* n (pow n (sub1 p)))))

(defn o% [n m] (if (o< n m) 0 (add1 (o% (o- n m) m))))

(o% 15 4)

(defn length [l] (if (null? l) 0 (add1 (length (cdr l)))))

(defn pick [i l] (cond (or (o< (length l) i) (null? l)) nil
                       (zero? (sub1 i)) (car l)
                       :else (pick (sub1 i) (cdr l))))


(defn no-nums [l] (cond (null? l) '()
                           (number? (car l)) (no-nums (cdr l))
                           :else (cons (car l) (no-nums (cdr l)))))

(no-nums '(0 "a" "b" 1 "c" 0))

(def all-nums #(cond (null? %) '()
                     (number? (car %)) (cons (car %) (all-nums (cdr %)))
                     :else (all-nums (cdr %))))

(all-nums '(0 "a" "b" 1 "c" 0))

(defn eqan? [o1 o2] (cond (and (number? o1) (number? o2)) (= o1 o2)
                          (and (atom? o1) (atom? o2)) (eq? o1 o2)
                          :else false))

(defn occur [a l] (cond (null? l) 0
                        (o= (car l) a) (add1 (occur a (cdr l)))
                        :else (occur a (cdr l))))
(occur 0 lat)

(def one? (comp zero? sub1))
(def one?2 (partial eq? 1))

(defn rempick [i l] (cond (or (o< (length l) i) (null? l)) nil
                          (one? i) (cdr l)
                          :else (cons (car l) (rempick (sub1 i) (cdr l)))))

(rempick 2 '(1 2 20 3 4))

(defn rember* [a l] (cond (null? l) '()
                          (atom? (car l)) 
                          (if (eq? a (car l)) (rember* a (cdr l)) (cons (car l) (rember* a (cdr l))))
                          :else (cons (rember* a (car l)) (rember* a (cdr l)))))

(rember* 1 '(1 (1) 1 111))

(defn insertR* [n o l] (cond (null? l) '()
                             (atom? (car l)) (if
                                              (eq? o (car l))
                                               (cons o (cons n (insertR* n o (cdr l))))
                                               (cons (car l) (insertR* n o(cdr l))))
                             :else (cons (insertR* n o (car l)) (insertR* n o (cdr l)))))

(insertR* 2 1 '(1 ((1)) 1 111))

(defn occur* [a [f & r :as l]] (cond (null? l) 0
                                     (atom? f) (if (eqan? f a)
                                                 (add1 (occur* a r))
                                                 (occur* a r))
                                     :else (o+ (occur* a f) (occur* a r))))

(defn subst* [n o [f & r :as l]] (cond (null? l) '()
                                       (atom? f) (if (eqan? f o) 
                                                   (cons n (subst* n o r))
                                                   (cons f (subst* n o r)))
                                       :else (cons (subst* n o f) (subst* n o r))))

(defn insertL* [n o l] (cond (null? l) '()
                             (atom? (car l)) (if
                                              (eq? o (car l))
                                               (cons n (cons o (insertL* n o (cdr l))))
                                               (cons (car l) (insertL* n o (cdr l))))
                             :else (cons (insertL* n o (car l)) (insertL* n o (cdr l)))))

(defn member* [a [f & r :as l]] (cond (null? l) false
                                      (atom? f) (or (eqan? f a) (member* a r))
                                      :else (or (member* a f) (member* a r))))

(occur* 2 '(1 ((1)) 1 1 2 11 2))

(defn leftmost [[f & r :as l]] (cond (null? l) '()
                                   (atom? f) f
                                   :else (leftmost f)))

(defn eqlist? [[f1 & r1 :as l1] [f2 & r2 :as l2]] (cond (null? l1) (null? l2)
                                                        (eq? f1 f2) (eqlist? r1 r2)
                                                        :else false))

(eqlist? '(1 2 3) '(1 2 3))
(leftmost '((((((1))))) (((2))) 2 111))





(defn numbered? [aexp] (cond (atom? aexp) (number? aexp)
                             (eq? (car (cdr aexp)) (quote +))
                             (and (numbered? (car aexp)) (numbered? (car (cdr (cdr aexp)))))
                             (eq? (car (cdr aexp)) (quote *)) 
                             (and (numbered? (car aexp)) (numbered? (car (cdr (cdr aexp)))))
                             (eq? (car (cdr aexp)) (quote pow)) 
                             (and (numbered? (car aexp)) (numbered? (car (cdr (cdr aexp)))))
                             :else false))

(numbered? '(1 + 2))

(defn applyfn [nexp rfn afn] (afn (rfn (car nexp)) (rfn (car (cdr (cdr nexp))))))
(defn value [nexp] (cond (atom? nexp) nexp
                         (eq? (car (cdr nexp)) (quote +)) (applyfn nexp value +)
                         (eq? (car (cdr nexp)) (quote *)) (applyfn nexp value *)
                         (eq? (car (cdr nexp)) (quote pow)) (applyfn nexp value pow)))

(value '(1 + 2))

