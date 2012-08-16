(ns run-clojure.using-expression-parser
 (:require [com.lithinos.amotoen.core :as amotoen]))




;; cf parsley parser generator
(defn parse-using-expression [string]

)

(defn using-expression-grammar 
{
    :expression [:term "x" :term]
 :term ['(% "x")]
})