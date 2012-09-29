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
       (fn [x] (list  x (array-map :name (second ast-ident-node)) )) 
       node-value))))

(defn ast-check-eq [ast-eq-node parameter]
  "Combines two value space with the eq operator.
 The eq operator binds the nth element of the first value space with
 the nth element of the second value space. 

For example, (= ((1) (2) (3)) ((a) (b) (c))) will create the value
space ((1 a) (2 b) (3 c)).

The eq operator can only be applied to value space containing the same
 number of tuples. Combining two value spaces with n tuples each will thus
 create a new value space also with n tuples.

Let d1 and d2 be the two combined value spaces.  If the tuples in d1
contain p1 parameter values and the tuples in d2 contain p2 parameter
values, then the tuples in d1=d2 will contain p1+p2 parameter values."
  (assert (= 'ast-eq (first ast-eq-node)))
  (let [term1 (ast-check-node (nth ast-eq-node 1) parameter)
        term2 (ast-check-node (nth ast-eq-node 2) parameter)]
    (if (not (= (count term1) (count term2)))
      (throw (Error. "'=' operation between parameters with
value space of different size."))
      (map-indexed (fn [i e] (concat e (nth term2 i))) term1))))

(defn ast-check-product [ast-product-node parameter]
"Combines two value spaces with the product operator. The product
operator create a new value space with a tuple for each possible
combination of a tuple in the first value space with a tuple in the
second value space. (Cartesian product.)

For example, (x ((1) (2)) ((a) (b))) will create the value
space ((1 a) (1 b) (2 a) (2 b)).

Let d1 and d2 be the two combined value spaces. If d1 has n1 tuples
and d2 has n2 tuples the resulting value space will have n1*n2 tuples.

If the tuples in d1 contain p1 parameter values and the tuples in d2
contain p2 parameter values, then the tuples in d1xd2 will contain
p1+p2 parameter values."

  (assert (= 'ast-product (first ast-product-node)))

)

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

(defn ast-check-and-process [ast parameter] 
  "Check the abstract syntax tree built from using expression and
  builds the tuples where each tuple represents a possible set of value for the parameters"
  (debug-print-return (ast-check-node ast parameter)))
