(ns run-clojure.cl_parsing)


(def using-expression-ast
  "Contains the abstract syntax tree of a using expression after its
  successful parsing, [] otherwise"
  [])

(def #^{:private true} grammar 
  "Grammar used to parse using expressions"
  {
   :up-case (amotoen/lpegs '| "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
   :numeric (amotoen/lpegs '| "1234567890")
   :ident [ :up-case '(* (| :numeric :up-case )  ) ]
   :term '(| [ \( :eq-expr \) ] 
              :ident)
   :prod-expr '(| [ :term \x :prod-expr ]
                  :term)
   :eq-expr '(| [ :prod-expr \= :eq-expr ]
                :prod-expr )
   :expr [ :eq-expr :$ ]
   })


(defn ast-remove-tag [x]
  (second (first x)))

(defn term-fn [x] 
  (if (= 3 (count x))
    (ast-remove-tag (second x))
    (list "ident" (ast-remove-tag x))))

(defn ident-fn [x]
  (reduce (fn [s t] (str s (second (first t))))
                    (str  (:up-case (first x)))
                    (rest (flatten x))))


(defn prod-expr-fn [x] 
  (if (= 3 (count x))
    (list "product" 
          (ast-remove-tag (first x))
          (ast-remove-tag (nth x 2)))
    (ast-remove-tag x)))

(defn expr-fn [x] (ast-remove-tag (first x)))

(defn eq-expr-fn [x]
  (if (= 3 (count x))
    (list "eq" (ast-remove-tag  (first x))
          (ast-remove-tag (nth x 2)))
    (ast-remove-tag x)))

(def #^{:private true} parse-using-expression-fns
  {:expr expr-fn
   :eq-expr eq-expr-fn
   :prod-expr prod-expr-fn
   :term term-fn
   :ident ident-fn
   })

(defn parse-using-expression [string]
  "Parses the using expression (-u switch)."
  (def using-expression-ast (amotoen/post-process :expr grammar 
                                                  (amotoen/wrap-string string)
                                                  parse-using-expression-fns))
  using-expression-ast)

