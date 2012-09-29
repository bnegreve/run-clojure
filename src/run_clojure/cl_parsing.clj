(ns run-clojure.cl_parsing
  (:use [clojure.tools.cli])
  (:use [run-clojure.utils])
  (:require [clojure.string :as string])
  (:require [com.lithinos.amotoen.core :as amotoen]))

(load "cl_parsing_parameters")
(load "cl_parsing_using_expression")

(defn parse-arguments [args]
  "Parse main program arguments. Builds the parameter list and the
using expression abstract syntax tree (ast)."
  (cli args
       ;; parameters 
       ["-p" "--parameter"
             "Provide a new parameter with its value space." 
             :parse-fn #(let [parsed-parameter (parse-parameter-description %)]
                          (assert (= (count parsed-parameter) 2))
                          (def parameters (assoc parameters 
                                            (first parsed-parameter) 
                                            (last parsed-parameter) ))
                          (debug-print-return parameters))]
       ;; using expression 
       ["-u" "--using"
        "Provide a using expression to describe the parameter value
space to be exlored."
        :parse-fn #(debug-print-return (parse-using-expression %))]
       
       ))
