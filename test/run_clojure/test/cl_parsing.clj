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
    (is (thrown? Exception
                 (parse-parameter-description "chicken")))
    (is (thrown? Exception
                 (parse-parameter-description "")))
    (is (thrown? Exception
                 (parse-parameter-description ":")))
    (is (thrown? Exception
                 (parse-parameter-description "poulet:a,b,c,")))
    (is (thrown? Exception
                 (parse-parameter-description "poulet:,a,b,c")))
    (is (thrown? Exception
                 (parse-parameter-description "poulet:,a,,b,c")))
    (is (thrown? Exception
                 (parse-parameter-description "poulet:,a,,b,c"))))


  (deftest should-work-when-called-from-general-argument-parsing-function
    (parse-arguments ["-p" "a:1,2"])
    (is (= parameters)
        (array-map "a" [1 2]))))