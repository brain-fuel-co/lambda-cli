(ns lambda-cli.core
  (:require [instaparse.core :as insta])
  (:gen-class))

(insta/set-default-output-format! :enlive)

(defn- read-input []
  (print "=> ")
  (flush)
  (read-line))

(def lambda-calculus
  (insta/parser
   "L_EXP  = VAR
           | LAMBDA VAR '.' L_EXP
           | L_EXP L_EXP
    VAR    = #'[a-zA-Z]'
    LAMBDA = 'lambda'"))

(defn- evaluate [input]
  (lambda-calculus input))

(defn -main
  "This will eventuall be a Lambda-Calculus REPL"
  [& args]
  (while true
    (let [input (read-input)]
      (println (evaluate input)))))

