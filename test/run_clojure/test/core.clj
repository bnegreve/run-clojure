(ns run-clojure.test.core
  (:use [run-clojure.core])
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
