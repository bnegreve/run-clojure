(ns run-clojure.core
  (:use [clojure.tools.cli])
  (:use [run-clojure.utils])
  (:require [clojure.string :as string])
  (:require [run-clojure.cl_parsing :as cl_parsing])
  (:require [run-clojure.utils :as utils]))

(defn build-parameter-tuples [parameters using-expression-ast]
"Builds a vector of parameter tuples from a using expression.
A parameter tuple is a tuples of parameter values that must be
replaced in a command line template to form one command line."
(println parameters) 
(println using-expression-ast)
)

(defn -main [& args]
  (cl_parsing/parse-arguments args)
  (cl_parsing/ast-check-and-process
   cl_parsing/using-expression-ast cl_parsing/parameters))

