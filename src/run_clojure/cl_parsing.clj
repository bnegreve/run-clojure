(ns run-clojure.cl_parsing
  (:use [clojure.tools.cli])
  (:require [clojure.string :as string])
  (:require [com.lithinos.amotoen.core :as amotoen]))

(load "cl_parsing_parameters")
(load "cl_parsing_using_expression")

; forward declaration
(def ast-check-node nil)

(defn ast-check-ident [ast-ident-node parameter]
  "Checks that ident has been declared in the parameter list, returns
parameter value space."
  (assert (= 'ast-ident (first ast-ident-node)))
  (let [node-value ((keyword (second ast-ident-node)) parameter)]
    (if (nil? node-value)
      (throw (Error. (str "Parameter '" (second ast-ident-node) "' undeclared.\n")))
      (map 
       (fn [x] (list  x (array-map :name (second ast-ident-node)) )) 
       node-value))))

(defn ast-check-eq [ast-eq-node parameter]
  "Combines to value space with the eq operator. The eq operator binds the nth element of the first term with the nth element of the second term.
 Combining two one parameter value space will thus create one two
parameters value space.  For example, (eq (1 2 3) (a b c)) will create
the value space (1 a) (2 b) (3 c) Each term value space must contain
the same number of elements. "
  (assert (= 'ast-eq (first ast-eq-node)))
  (let [term1 (ast-check-node (nth ast-eq-node 1) parameter)
        term2 (ast-check-node (nth ast-eq-node 2) parameter)]
    (if (not (= (count term1) (count term2)))
      (throw (Error. "'=' operation between parameters with
value space of different size."))
      (map-indexed (fn [i e] (concat e (nth term2 i))) term1))))

(defn ast-check-node [ast parameter] 
  "Checks the node of the abstract syntax tree and decorates the tree
with tuples of values for parameters."
  ; replace with switch case
  (let [node-label (first ast)]
  (if (= node-label 'ast-ident)
    (ast-check-ident ast parameter)
    (if (= node-label 'ast-eq)
      (ast-check-eq ast parameter)
      (assert false))))) ; node label is unkown


(defn ast-check [ast parameter] 
  "Check the abstract syntax tree built from using expression and
  builds the tuples where each tuple represents a possible set of value for the parameters"
  (ast-check-node ast parameter))


(defn check-using-expression-context [using-expression-ast parameters]
(println using-expression-ast)
(ast-check using-expression-ast parameters)
true)

    
(defn parse-arguments [args]
  "Parse main program arguments."
  (cli args
       ;; parameters 
       ["-p" "--parameter"
             "Provide a new parameter with its value space." 
             :parse-fn #(let [parsed-parameter (parse-parameter-description %)]
                          (assert (= (count parsed-parameter) 2))
                          (def parameters (assoc parameters 
                                            (first parsed-parameter) 
                                            (last parsed-parameter) )))]
       ;; using expression 
       ["-u" "--using"
        "Provide a using expression to describe the parameter value
space to be exlored."
        :parse-fn #(parse-using-expression %)]
       
       ))
