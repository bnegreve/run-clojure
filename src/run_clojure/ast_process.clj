(ns run-clojure.ast_process
    (:use [run-clojure.utils]))

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
       (fn [x] (array-map 
                (keyword (second ast-ident-node))
                x)) 
       node-value))))

(defn ast-check-eq [ast-eq-node parameter]
  "Combines two value spaces with the eq ('=') operator.
Value spaces are made of tuples. Each tuple is actually a map which
 binds (one or more) parameter names with values.

When two value spaces are combined with the eq operator ('=') the
tuples from the first input value space are merged with the tuples of
the second input value space. (The operation is rejected if two two
input value spaces have different number of tuples.)

Hence: 
- There are as many tuple in the new value space as in any
input value space.

- The size of the tuples in the new value space is the sum of the size
  of the tuples in the input value spaces. (Unless there are duplicate
  parameter names (e.g. A=A))

For example:
input value space 1 (two tuples for 2 values of parameter A):
 ({:A 1} {:A 2})  

input value space 2 (two tuples for 2 values of parameter B):
 ({:B 10} {:B 20}) 

new value space (two tuples for 2 values of parameter A and B)
 ({:A 1, :B 10}
  {:A 2,  :B 20})"
  (assert (= 'ast-eq (first ast-eq-node)))
  (let [term1 (ast-check-node (nth ast-eq-node 1) parameter)
        term2 (ast-check-node (nth ast-eq-node 2) parameter)]
    (if (not (= (count term1) (count term2)))
      (throw (Error. "'=' operation between parameters with
value space of different size."))
      (map-indexed (fn [i e] (merge e (nth term2 i))) term1))))

(defn ast-check-product [ast-product-node parameter]

  "Combines two value spaces with the product ('x') operator.
Value spaces are made of tuples. Each tuple is actually a map which
 binds (one or more) parameter names with values.

When two value spaces are combined with the product operator ('x') the
tuples from the first input value space are combined with the tuples of
the second input value space. 

Hence: 
- The number of tuples in the new value space is the number of tuples in the first input value space times the number of tuples in the second input value space.

- The size of the tuples in the new value space is the sum of the size
  of the tuples in the input value spaces. (Unless there are duplicate
  parameter names (e.g. A=A))

For example:
input value space 1 (two tuples for 2 values of parameter A):
 ({:A 1} {:A 2})  

input value space 2 (two tuples for 2 values of parameter B):
 ({:B 10} {:B 20}) 

new value space (two tuples for 2 values of parameter A and B)
 ({:A 1, :B 10}
  {:A 1, :B 20}
  {:A 2, :B 10}
  {:A 2, :B 20})"
(assert (= 'ast-product (first ast-product-node)))
(let [term1 (ast-check-node (nth ast-product-node 1) parameter)
      term2 (ast-check-node (nth ast-product-node 2) parameter)]
  (reduce (fn [c x] (concat x c)) '() 
          (map (fn [t1] 
                 (map (fn [t2] 
                        (merge t1 t2)) term2))
               term1))))
               
(defn ast-check-node [ast parameter] 
  "Checks the node of the abstract syntax tree and decorates the tree
with tuples of values for parameters."
  ; replace with switch case
  (let [node-label (first ast)]
  (if (= node-label 'ast-ident)
    (ast-check-ident ast parameter)
    (if (= node-label 'ast-eq)
      (ast-check-eq ast parameter)
      (if (= node-label 'ast-product)
        (ast-check-product ast parameter)
        (assert false)))))) ; node label is unkown

(defn ast-check-and-process [ast parameter] 
  "Check the abstract syntax tree built from using expression and
  builds the tuples where each tuple represents a possible set of value for the parameters"
  (debug-print-return (ast-check-node ast parameter)))
