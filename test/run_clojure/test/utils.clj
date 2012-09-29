(ns run-clojure.test.utils
  (:use [clojure.test])
  (:use [run-clojure.utils]))

(testing "debug-print-return"
  (deftest should-always-return-the-same-value
    (is (= '(a b c) (debug-print-return '(a b c))))
    (is (= 1 (debug-print-return 1)))
    (is (= 3 (debug-print-return 1 2 3))))) ; can only return one argument


