(ns run-clojure.cl_parsing
  (:use [clojure.tools.cli])
  (:require [clojure.string :as string]))

(load "cl_parsing_parameters")

(defn parse-arguments [args]
  "Parse main program arguments."
  (cli args ["-p" "--parameter"
             "Provide a new parameter with its value space." 
             :parse-fn #(let [parsed-parameter (parse-parameter-description %)]
                          (assert (= (count parsed-parameter) 2))
                          (def parameters (assoc parameters 
                                            (first parsed-parameter) 
                                            (last parsed-parameter) )))]))

