(ns run-clojure.test.ast_process
  (:use [clojure.test])
  (:use [run-clojure.ast_process])
  (:use [run-clojure.cl_parsing]))

(testing "ast-check-and-process"
  (deftest should-detect-undeclared-parameters
    (is (thrown? Error
                 (ast-check-and-process 
                  '(ast-eq (ast-ident A) (ast-ident C))
                  {:A [1 2 4]}
                  ))))

  (deftest should-handle-single-ident
    (is (= '((1 {:name A}) (2 {:name A}) (4 {:name A}))
            (ast-check-and-process 
            '(ast-ident A) ; ast 
            {:A [1 2 4]})))) ; parameter 
  (deftest should-handle-eq
    (is (= '((1 {:name A} 10 {:name B}) (2 {:name A} 20 {:name B}) (4 {:name A} 30 {:name B}))
           (ast-check-and-process 
            '(ast-eq (ast-ident A) (ast-ident B)) ; ast
            {:B [10 20 30], :A [1 2 4]})))) ; parameters 

  (deftest should-handle-product
    (let [parameters 
          (add-parsed-parameter 
           (add-parsed-parameter (array-map) (parse-parameter-description "A:1,2"))
           (parse-parameter-description "B:a"))]
      (is (= '(("1" {:name "A"} "a" {:name "B"}) ("2" {:name "A"} "a" {:name "B"}))
           (ast-check-and-process 
;            '(ast-product (ast-ident A) (ast-ident B)) ; ast
            (parse-using-expression "AxB")
            parameters))))) ; parameters 

  (deftest should-handle-double-product
        (let [parameters 
          (add-parsed-parameter 
           (add-parsed-parameter (array-map) (parse-parameter-description "A:1,2"))
           (parse-parameter-description "B:a"))]
          (is (= '(("a" {:name "B"} "1" {:name "A"} "a" {:name "B"} "a" {:name "B"} "2" {:name "A"} "a" {:name "B"}))
                 (ast-check-and-process 
                  (parse-using-expression "BxAxB") parameters)))))
)
