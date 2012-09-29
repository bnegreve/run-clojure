(ns run-clojure.test.cl_parsing
  (:use [clojure.test])
  (:use [run-clojure.cl_parsing]))

(testing "parameter parsing"
  (deftest should-handle-simple-parameter-description
    (is (= (list :Chicken (vector "1"))
           (parse-parameter-description "Chicken:1")))
    (is (= (list :chicken (vector "1" "2" "3"))
           (parse-parameter-description "chicken:1,2,3")))
    (is (= (list :chicken_size (vector "1" "2" "3"))
           (parse-parameter-description "chicken_size:1,2,3")))
    (is (= (list :chicken_size (vector "a:" "b/Q" "c9@"))
           (parse-parameter-description "chicken_size:a:,b/Q,c9@"))))

  (deftest should-handle-syntax-errors-in-parameter-description
    (is (thrown? Error
                 (parse-parameter-description "chicken")))
    (is (thrown? Error
                 (parse-parameter-description "")))
    (is (thrown? Error
                 (parse-parameter-description ":")))
    (is (thrown? Error
                 (parse-parameter-description "poulet:a,b,c,")))
    (is (thrown? Error
                 (parse-parameter-description "poulet:,a,b,c")))
    (is (thrown? Error
                 (parse-parameter-description "poulet:,a,,b,c")))
    (is (thrown? Error
                 (parse-parameter-description "poulet:,a,,b,c"))))
  (deftest should-work-when-called-from-general-argument-parsing-function
    (parse-arguments ["-p" "a:1,2"])
    (is (= parameters)
        (array-map "a" [1 2]))))

(testing "using expression parsing"
  (deftest should-handle-simple-using-expression
    (is (= '(ast-ident "A")
           (parse-using-expression "A")))
    (is (= '(ast-ident "A")
           (parse-using-expression "(A)")))
    (is (= '(ast-eq (ast-ident "A") (ast-ident "A"))
           (parse-using-expression "A=A")))
    (is (= '(ast-product (ast-ident "A") (ast-ident "A"))
           (parse-using-expression "AxA")))))

(testing "using expression parsing"
  (deftest should-deal-with-priority
    (is (= '(ast-product (ast-ident "A") (ast-eq (ast-ident "B") (ast-ident "C"))
           (parse-using-expression "AxB=C"))))
    (is (= '(ast-eq (ast-ident "A") (ast-product (ast-ident "B") (ast-ident "C")))
           (parse-using-expression "A=BxC")))
    (is (= '(ast-product (ast-eq (ast-ident "A") (ast-ident "B")) (ast-ident "C"))
           (parse-using-expression "(A=B)xC")))
))
