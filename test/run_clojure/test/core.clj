(ns run-clojure.test.core
  (:use [run-clojure.core])
  (:use [run-clojure.cl_parsing])
  (:use [clojure.test])
  (:use  com.lithinos.amotoen.core
         com.lithinos.amotoen.grammars.csv
         clojure.pprint))

(testing "core"
  (deftest dummy
    (is true)))

(testing "Amotoen"
    (deftest amotoen-should-be-available
      (is (= [["a" "b" "c"]] (to-clj "a,b,c")))))

(testing "Command line building"
  (deftest should-work-with-simple-command-line
    (let [parameters 
              (add-parsed-parameter
               (add-parsed-parameter 
                (add-parsed-parameter (array-map) (parse-parameter-description "A:1,2"))
                (parse-parameter-description "B:a")) (parse-parameter-description "C:x"))]
      (is (= "./test 1" 
             (substitute-parameter-name "./test A" {:A 1} "A")))
    (is (= "./test 1"
           (build-command-line-string "./test A" parameters {:A "1"})))
    (is (= "./test 2 3"
           (build-command-line-string "./test A AB" parameters {:A "2", :AB "3"})))
    (is (= "./test 2 2"
           (build-command-line-string "./test A A" parameters {:A "2"})))
    (is (= "./test ab abc"
           (build-command-line-string "./test A B" parameters {:A "ab", :B "abc"})))
    (is (= "./test 3 2"
           (build-command-line-string "./test B A" parameters {:A "2", :B "3"})))
)))
    
