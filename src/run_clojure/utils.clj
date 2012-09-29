(ns run-clojure.utils)

(def debug true)

(defn debug-print-return [& more]
  "Prints its arguments if debug is set and return the last
argument (whatever the state of debug)."
  (if debug
    (let []
      (print "DEBUG: ")
      (doseq [s more] (print s))
      (println)
      (last more))
    (last more)))

