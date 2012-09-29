(ns run-clojure.test.ast_process
  (:use [clojure.test])
  (:use [run-clojure.ast_process]))

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

)
