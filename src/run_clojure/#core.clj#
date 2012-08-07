(ns run-clojure.core
  (:use [clojure.tools.cli]
        [clojure.string]))




(defn parse-parameter-value-space [string]
 "Given a string representing a list of values separated by commas
this function returns an array containing each value."
  (split string #","))

(defn parse-parameter-description [string]
  "Parse a parameter string description <parameter name>:<parameter
 value 1>,...,<parameter value n>, returns a list containing parameter
 name followed by an array of values which represent the value space."
  (let [parameter-string (split string #":" 2)]
    (list (first parameter-string)
          (parse-parameter-value-space (last parameter-string)))))

;;(println (parse-parameter-description "XXX 1,3,2,1"))


(defn parse-arguments [args]
  "Parse main program arguments."
  (cli args ["-p" "--parameter" "Provide a new parameter with its value space." 
             :parse-fn #(parse-parameter-description %)]))

(defn -main [& args]
  (println (parse-arguments args)))


;; (println (cli args ["-p" "--port" "Port to listen on" :default 3000 :parse-fn #(Integer/parseInt %)]))



