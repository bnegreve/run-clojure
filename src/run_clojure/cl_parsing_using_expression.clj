(ns run-clojure.cl_parsing)


(def using-expression-ast
  "Contains the abstract syntax tree of a using expression after its
  successful parsing, [] otherwise"
  [])

;; (def #^{:private true} grammar 
;;   "Grammar used to parse using expressions"
;;   {:Expression [:Term '(* [:Operator :Term] ) :$]
;;    :Operator [ \x ]
;;    :Term [ \a ]
;;    })


(def #^{:private true} grammar 
  "Grammar used to parse using expressions"
  {
   :up-case (amotoen/lpegs '| "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
   :numeric (amotoen/lpegs '| "1234567890")
   :ident [ :up-case '(* (| :numeric :up-case )  ) ]
   :term '(| :ident [ \( :eq-expr \) ])
   :prod-expr '(| :term \x :prod-expr
                  :term)
   :eq-expr '(| [ :prod-expr \= :eq-expr ]
                :prod-expr )
   :expr [ :eq-expr :$ ]
   })


(defn expression-fn [x]
  (println "expression" x)
  (if (= (count x) 3) ;; expression is term operator expression
    (list "op" (nth x 0) (nth x 2))
    ;; else term is promoted to expression, return the term itself
    x))
  

(defn operator-fn [x]
  (println "operator" x)x)

(defn term-fn [x]
    (println "term" x)x)

(defn ident-fn [x]
  (reduce (fn [s t] (str s (second (first t))))
                    (str  (:up-case (first x)))
                    (rest (flatten x))))

;; (def #^{:private true} parse-using-expression-fns
;;   {:Expression expression-fn
;;    :Term term-fn
;;    :Operator operator-fn
;;    })

(def #^{:private true} parse-using-expression-fns
  {:expr identity
   :eq-expr identity
   :prod-expr identity
   :term identity
   :ident ident-fn
   })

(defn parse-using-expression [string]
  "Parses the using expression (-u switch)."
  (def using-expression-ast (amotoen/post-process :expr grammar 
                                                  (amotoen/wrap-string string)
                                                  parse-using-expression-fns)))

