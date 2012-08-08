(ns run-clojure.test.core
  (:use [run-clojure.core])
  (:use [clojure.test]))

;; (defn check-parsed-parameter [parsed-parameter expected-value-space-size]
;;   (if (= (count parsed-parameter) 2)
;;     (if  (vector? (last parsed-parameter))
;;       (if (= (count (last parsed-parameter))            
;;                   expected-value-space-size)
;;         true)))
;;   nil)

(testing "parameter parsing"
  (deftest should-handle-simple-parameter-description
    (is (= (list "Chicken" (vector "1"))
           (parse-parameter-description "Chicken:1")))
    (is (= (list "chicken" (vector "1" "2" "3"))
           (parse-parameter-description "chicken:1,2,3")))
    (is (= (list "chicken_size" (vector "1" "2" "3"))
           (parse-parameter-description "chicken_size:1,2,3")))
    (is (= (list "chicken_size" (vector "a:" "b/Q" "c9@"))
           (parse-parameter-description "chicken_size:a:,b/Q,c9@")))))

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