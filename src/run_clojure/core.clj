(ns run-clojure.core
  (:use [clojure.tools.cli])
  (:use [run-clojure.utils])
  (:require [clojure.string :as string])
  (:require [run-clojure.cl_parsing :as cl_parsing])
  (:require [run-clojure.ast_process :as ast_process]))


(defn -main [& args]
  (cl_parsing/parse-arguments args)
  (ast_process/ast-check-and-process
   cl_parsing/using-expression-ast cl_parsing/parameters))

