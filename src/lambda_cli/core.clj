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
   "L_EXP      = VAR_EXP
               | APPLY
               | ABSTRACT
    APPLY      = <'('> L_EXP <WHITESPACE> L_EXP <')'>
    ABSTRACT   = LAMBDA VAR BIND L_EXP
    BIND       = '.' | '->'
    WHITESPACE = #'\\s+'
    VAR_EXP    = VAR
               | NUMBER
               | NUMBER VAR
               | VAR_EXP ARITH_OP VAR_EXP
    VAR        = #'[a-z]' | #'[A-Z]+'
    NUMBER     = #'[0-9]+'
    ARITH_OP   = ADD | SUB | MUL | DIV
    ADD        = <'+'>
    SUB        = <'-'>
    MUL        = <'*'>
    DIV        = <'/'>
    LAMBDA     = 'lambda' | 'λ' | '\\\\'"
   :auto-whitespace :standard))

;;(defn- evaluate-var-exps)

(defn- evaluate [input]
  (->> (lambda-calculus input)))

(defn -main
  "This will eventually be a Lambda-Calculus REPL"
  [& args]
  (while true
    (let [input (read-input)]
      (-> input
          evaluate
          pp/pprint))))
