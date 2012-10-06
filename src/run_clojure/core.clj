(ns run-clojure.core
  (:use [clojure.tools.cli])
  (:use [run-clojure.utils])
  (:require [clojure.string :as string])
  (:require [run-clojure.cl_parsing :as cl_parsing])
  (:require [run-clojure.ast_process :as ast_process]))

(defn substitute-parameter-name [command-line-template tuple parameter-name]
  "Parses command-line-template and replace all the instaces of
parameter-name with its corresponding value in tuple (:na"
  (if (re-find (re-pattern parameter-name) command-line-template)
    (string/replace command-line-template parameter-name
                    (str ((keyword parameter-name) tuple)))
    (or (println "Warning: Cannot find '" parameter-name "' 
in command line template.") 'command-line-template)))

 (defn build-command-line-string [command-line-template parameters tuple]
  "Builds a command line string from a tuple. Parameter names (from
  parameters array) in the command line template shall be replaced by
  the corresponding values in the tuple.  Identical parameter names
  will always be replaced with the same value.  Substitution are made
  from longest parameters to shortest parameters to avoid ambiguity in case of a parameter name is a prefix of another one (e.g. A and AB)"
  (debug-print-return "Tuple: " tuple)
  (reduce 
   (fn [command-line next-parameter-name]
     (println next-parameter-name)
     (substitute-parameter-name command-line tuple next-parameter-name)) 
   command-line-template
   (sort (fn [a b] (>= (count a) (count b))) ; parameter names
                                               ; sorted to avoid
                                               ; prefix abiguity
           (cl_parsing/all-parameter-names tuple))))

(defn -main [& args]
  (cl_parsing/parse-arguments args)
  (map (fn [x]
         (build-command-line-string "./azkj A B C" cl_parsing/parameters x))
       (ast_process/ast-check-and-process
        cl_parsing/using-expression-ast cl_parsing/parameters)))

