(ns lambda-cli.core
  (:require [instaparse.core :as insta])
  (:gen-class))

;;(insta/set-default-output-format! :enlive)

(defn- read-input []
  (print "=> ")
  (flush)
  (read-line))

(def lambda-expressions
  (insta/parser
   "L-EXP = (VARIABLE_EXPRESSION | APPLICATION | ABSTRACTION) <WHITESPACE*>
    VARIABLE_EXPRESSION = <WHITESPACE*> (NUMBER* | VARIABLE ( <WHITESPACE*> OPERATOR VARIABLE_EXPRESSION)*)
    NUMBER = <WHITESPACE*> #'[0-9]'
    VARIABLE = <WHITESPACE*> #'[a-zA-Z]'
    OPERATOR = <WHITESPACE*> ('+' | '-' | '*' | '/')
    APPLICATION = <WHITESPACE*> L-EXP L-EXP
    ABSTRACTION = <WHITESPACE*> 'lambda' <WHITESPACE*> VARIABLE <WHITESPACE*> '.' <WHITESPACE*> L-EXP
    WHITESPACE = #'\\s'"))

   

(defn- evaluate [input]
  (lambda-expressions input))

(defn -main
  "This will eventuall be a Lambda-Calculus REPL"
  [& args]
  (while true
    (let [input (read-input)]
      (println (evaluate input)))))
