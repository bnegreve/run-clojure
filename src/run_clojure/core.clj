(ns run-clojure.core
  (:use [clojure.tools.cli])
  (:require [clojure.string :as string])
  (:use [run-clojure.cl_parsing]))

(defn -main [& args]
  (parse-arguments args)
  (println parameters))

