(ns run-clojure.core
  (:use [clojure.tools.cli]
        [clojure.string]))


(defn parameter-description-usage-string []
  "Return parameter description usage string."
  "<parameter name>:<parameter value 1>,...,<parameter value n>")

(defn parameter-name-re []
  "Parameter names must match the regexp  returned by this function."
  #"^[a-zA-Z][a-zA-Z0-9_]*$")

(defn parameter-value-space-re []
  "Parameter value space description must match the regexp returned by
this function."  
  #"^[^,]+(,[^,]+)*$")

(defn parameter-name-is-correct [string]
  "Returns true if parameter's name matches parameter's format, throws
an exception otherwise."
  (if (not (re-seq (parameter-name-re) string))
    (throw (Exception. 
            (str "Parameter name format is incorrect."
                 "(Parameter name must match the following regular expression: "
                 (str (parameter-name-re)))))
    true))

(defn parameter-value-space-is-correct [string] 
  "Returns true if parameter's name matches value space format, throws
an exception otherwise."
  (if (not (re-seq (parameter-value-space-re) string))
    (throw (Exception. 
            (str "Parameter value space format is incorrect."
                 "(Parameter value space must match the following
regular expression: "
                 (str (parameter-value-space-re)))))
    true))

(defn parse-parameter-value-space [string]
 "Given a string representing a list of values separated by commas
this function returns an array containing each value."
  (split string #","))

(defn parse-parameter-name [string]
  "Parse parameter name (noop)."
  string)

(defn parse-parameter-description [string]
  "Parse a parameter description string."
  (let [description (split string #":" 2)]
    (if (not (= (count  description) 2))
      (throw (Exception. (str "Parameter description format is incorrect,"
                              " format must be "
                              (parameter-description-usage-string) ".")))
      (if (and (parameter-name-is-correct (first description))
               (parameter-value-space-is-correct (last description)))
        (list (parse-parameter-name (first description))
              (parse-parameter-value-space (last description)))
        (assert nil "Unhanded systax error in parameter description.")))))
    

;; (parse-parameter-description "poulet:1,2,3")
;; (parse-parameter-description "Poul2et:qsd")
;; (parse-parameter-description "Poul2et:1qsd,2qj,3lkqsj")
;; (parse-parameter-description "Poul2et:1q:qds,qd!")

;; (parse-parameter-description "Poul2et:")
;; (parse-parameter-description ":qdkj")
;; (parse-parameter-description ":qdkj")
;; (parse-parameter-description "qsdkj:qdkj,qds,q,")
;; (parse-parameter-description "qsdkj:,qdkj,qds,q")









(defn parse-arguments [args]
  "Parse main program arguments."
  (cli args ["-p" "--parameter" "Provide a new parameter with its value space." 
             :parse-fn #(parse-parameter-description %)]))

(defn -main [& args]
  (println (parse-arguments args)))


;; (println (cli args ["-p" "--port" "Port to listen on" :default 3000 :parse-fn #(Integer/parseInt %)]))



