
(def parameters "Contains the list of user defined parameters (-p)
bound with their value space." (array-map))


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
    (throw (Error. 
            (str "Parameter name format is incorrect."
                 "(Parameter name must match the following regular expression: "
                 (str (parameter-name-re)))))
    true))

(defn parameter-value-space-is-correct [string] 
  "Returns true if parameter's name matches value space format, throws
an exception otherwise."
  (if (not (re-seq (parameter-value-space-re) string))
    (throw (Error. 
            (str "Parameter value space format is incorrect."
                 "(Parameter value space must match the following
regular expression: "
                 (str (parameter-value-space-re)))))
    true))

(defn parse-parameter-value-space [string]
 "Given a string representing a list of values separated by commas
this function returns an array containing each value."
 (string/split string #","))

(defn parse-parameter-name [string]
  "Parse parameter name (noop)."
  string)

(defn parse-parameter-description [string]
  "Parse a parameter description string."
  (let [description (string/split string #":" 2)]
    (if (not (= (count  description) 2))
      (throw (Error. (str "Parameter description format is incorrect,"
                              " format must be "
                              (parameter-description-usage-string) ".")))
      (if (and (parameter-name-is-correct (first description))
               (parameter-value-space-is-correct (last description)))
        (list (keyword (parse-parameter-name (first description)))
              (parse-parameter-value-space (last description)))
        (assert nil "Unhanded systax error in parameter description.")))))

(defn add-parsed-parameter [parameters new-parsed-parameter]
  (assoc parameters 
    (first new-parsed-parameter) 
    (last new-parsed-parameter)))

(defn all-parameter-names [parameters]
  "Returns the list of every parameter name."
  (map name (keys parameters)))
