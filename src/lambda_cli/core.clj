(ns lambda-cli.core
  (:require [clojure.pprint :as pp]
            [net.cgrand.enlive-html :as enlive]
            [instaparse.core :as insta])

  (:gen-class))

(insta/set-default-output-format! :enlive)

(defn- read-input []
  (print "=> ")
  (flush)
  (read-line))

(def lambda-calculus
  (insta/parser
   "L_EXP    = VAR_EXP
             | LAMBDA VAR '.' L_EXP
             | <'('> L_EXP <')'> <'('> L_EXP <')'>
    VAR_EXP  = VAR
             | NUMBER
             | NUMBER VAR
             | VAR_EXP ARITH_OP VAR_EXP
    VAR      = #'[a-z]' | #'[A-Z]+'
    NUMBER   = #'[0-9]+'
    ARITH_OP = ADD | SUB | MUL | DIV
    ADD      = <'+'>
    SUB      = <'-'>
    MUL      = <'*'>
    DIV      = <'/'>
    LAMBDA   = 'lambda'"
   :auto-whitespace :standard))

(defn- eliminate-nil-fields [input]
   (->> input))
    


 


(defn- evaluate [input]
  (->> (lambda-calculus input)
       eliminate-nil-fields))

(defn -main
  "This will eventuall be a Lambda-Calculus REPL"
  [& args]
  (while true
    (let [input (read-input)]
      (-> input
          evaluate
          pp/pprint))))
