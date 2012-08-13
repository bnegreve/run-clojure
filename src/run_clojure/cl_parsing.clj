(ns run-clojure.cl_parsing
  (:use [clojure.tools.cli])
  (:require [clojure.string :as string]))

(load "cl_parsing_parameters")
(load "cl_parsing_using_expression")

(defn parse-arguments [args]
  "Parse main program arguments."
  (cli args
       ;; parameters 
       ["-p" "--parameter"
             "Provide a new parameter with its value space." 
             :parse-fn #(let [parsed-parameter (parse-parameter-description %)]
                          (assert (= (count parsed-parameter) 2))
                          (def parameters (assoc parameters 
                                            (first parsed-parameter) 
                                            (last parsed-parameter) )))]
       ;; using expression 
       ["-u" "--using"
        "Provide a using expression to describe the parameter value
space to be exlored."
        :parse-fn #(parse-using-expression %)]
       
       ))


