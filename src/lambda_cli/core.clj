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
    APPLY      = <'('> L_EXP L_EXP <')'>
    ABSTRACT   = LAMBDA VAR BIND L_EXP
    BIND       = '.' | '->'
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
(defn not-arith-op [tag]
  (case tag
    (:ADD :SUB :MUL :DIV) false
    true))

(defn mk-application
  [coll]
  (str "(" (apply str (interpose " " coll)) ")"))

(defn mk-reduction
  [coll]
  (str "[" (apply str (interpose ":=" coll)) "]"))

(defn reconstruct [m]
  (let [tag (:tag m) exps (:content m)]
  (cond
    (and (not-arith-op tag) (nil? exps)) m

    (= tag :APPLY)        (mk-application
                           (map reconstruct exps))
    (or (= tag :ABSTRACT)
        (= tag :VAR_EXP)) (apply str (map reconstruct exps))

;;    (= tag :REDUCE)       (str (reconstruct (first exps))
;;                               (mk-reduction (map reconstruct (rest exps))))

    (= tag :LAMBDA)       "λ"
    (= tag :BIND)         "."

    (= tag :ADD)          "+"
    (= tag :SUB)          "-"
    (= tag :MUL)          "*"
    (= tag :DIV)          "/"

    :else                 (reconstruct (first exps)))))

(defn bifurcate [application]
  (if (= :APPLY (:tag application))
    (map reconstruct (:content application))
    (bifurcate (first (:content application)))))

(defn deconstruct-lambda [lambda]
  (if (= (:tag lambda) :ABSTRACT)
    {:BINDING_VAR (reconstruct (second (:content lambda)))
     :L_EXP (reconstruct (nth (:content lambda) 3))}
    (deconstruct-lambda (first (:content lambda)))))

(defn app-to-abstraction? [application]
  (let [exp (first (:content application))]
    (if (= (:tag (first (:content exp))) :ABSTRACT) true false)))

(defn can-sub? [exp]
    (let [c (first (:content exp)) tag (:tag c)]
      (if (= tag :APPLY)
        (if (app-to-abstraction? c) true false) false)))

(defn sub [exp]
  (let [bifurcation (bifurcate exp) to-update (deconstruct-lambda (evaluate (first bifurcation))) replacement (second bifurcation)]
    (clojure.string/replace
     (:L_EXP to-update) (:BINDING_VAR to-update) replacement)))

(defn β-reduce [exp]
  (if (can-sub? exp) (sub exp) (reconstruct exp)))

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
