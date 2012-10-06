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
    (is (= '({:A 1} {:A 2} {:A 4})
            (ast-check-and-process 
            '(ast-ident A) ; ast 
            {:A [1 2 4]})))) ; parameter 
  (deftest should-handle-eq
    (is (= '({:A 1, :B 10} {:A 2, :B 20} {:A 4, :B 30})
           (ast-check-and-process 
            '(ast-eq (ast-ident A) (ast-ident B)) ; ast
            {:B [10 20 30], :A [1 2 4]})))) ; parameters 

  (deftest should-handle-product
    (let [parameters 
          (add-parsed-parameter 
           (add-parsed-parameter (array-map) (parse-parameter-description "A:1,2"))
           (parse-parameter-description "B:a"))]
      (is (=  '({:A "2", :B "a"} {:A "1", :B "a"})
             (ast-check-and-process 
              (parse-using-expression "AxB")
              parameters))))) ; parameters 

  (deftest should-handle-double-product
        (let [parameters 
              (add-parsed-parameter
               (add-parsed-parameter 
                (add-parsed-parameter (array-map) (parse-parameter-description "A:1,2"))
                (parse-parameter-description "B:a")) (parse-parameter-description "C:x"))]
          (is (= '({:B "a", :A "2", :C "x"} {:B "a", :A "1", :C "x"})
                 (ast-check-and-process 
                  (parse-using-expression "BxAxC") parameters)))))
)
