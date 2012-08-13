(ns run-clojure.test.core
  (:use [run-clojure.core])
  (:use [clojure.test]))

(testing "core"
  (deftest dummy
    (is true)))