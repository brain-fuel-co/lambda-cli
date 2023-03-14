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
   "L_EXP    = VAR_EXP
             | LAMBDA VAR_EXP '.' L_EXP
             | L_EXP L_EXP
    VAR_EXP  = VAR
             | NUMBER VAR
             | VAR_EXP ARITH_OP VAR_EXP
    VAR      = #'[a-zA-Z]'
    NUMBER   = #'[0-9]'
    ARITH_OP = '+' | '-' | '*' | '/'
    LAMBDA   = 'lambda'"
   :auto-whitespace :standard))

(defn- evaluate [input]
  (lambda-calculus input))

(defn -main
  "This will eventuall be a Lambda-Calculus REPL"
  [& args]
  (while true
    (let [input (read-input)]
      (println (evaluate input)))))

