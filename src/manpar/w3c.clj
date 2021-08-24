(ns manpar.w3c
  (:require [instaparse.core :as insta]
            [cheshire.core :as cs])
  (:gen-class))

;This follows the Grammar for Manchester Syntax (but we cannot parse IRIs with the symbols '(' or ')' )
(def parser
  (insta/parser
   "S = Description 
    Description = Conjunction <WS> <'or'> <WS> Conjunction {<WS> <'or'> <WS> Conjunction }
                | Conjunction
    Conjunction = classIRI <WS> <'that'> <WS> [ 'not' <WS> ] Restriction { <WS> 'and' <WS> [ 'not' <WS> ] Restriction }
                | Primary <WS> <'and'> <WS>  Primary { <WS> <'and'> <WS> Primary }
                | Primary
    Primary = [ 'not' <WS> ] ( Restriction | atomic )

    Restriction = ObjectPropertyExpression <WS> 'some' <WS> Primary 
                | ObjectPropertyExpression <WS> 'only' <WS> Primary 
                | ObjectPropertyExpression <WS> 'value' <WS> Individual
                | ObjectPropertyExpression <WS> 'Self'
                | ObjectPropertyExpression <WS> 'min' <WS> NonNegativeInteger <WS> [ Primary ]
                | ObjectPropertyExpression <WS> 'max' <WS> NonNegativeInteger <WS> [ Primary ]
                | ObjectPropertyExpression <WS> 'exactly' <WS> NonNegativeInteger <WS> [ Primary ]
                | ObjectPropertyExpression <WS> 'value' <WS> Literal

    <atomic> = classIRI
           | <'('> Description <')'>

    WS = #'\\s+' 

    NonNegativeInteger = Zero | PositiveInteger
    PositiveInteger = NonZero { digit }
    <digit> = Zero | NonZero
    <Zero> = '0'
    <NonZero> = '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' 

    Literal = #'.*'
    Individual = #'.*'
    classIRI = IRI
    objectPropertyIRI = IRI
    <IRI> = #'[^ ()\\s]*'
    ObjectProperty = #'\\S*'
    ObjectPropertyExpression = objectPropertyIRI 
                             | InverseObjectProperty
    InverseObjectProperty = <WS> 'inverse' <WS> objectPropertyIRI
    "))

(defn translate-description
  [& arg]
  (let [[args] arg] 
    (if (= (count args) 1)
      (first args)
      (vec (cons "UnionOf" args)))))

(defn translate-conjunction
  [& arg]
  (let [[args] arg]
    (if (= (count args) 1)
      (first args)
      (vec (cons "IntersectionOf" args)))))

(defn translate-primary
  [& arg]
  (let [[args] arg
        [inner] (rest args)]
    (if (= (count args) 1)
      (first args)
      (vector "ComplementOf" inner)))) ; not args


(defn translate-restriction-2
  [arg]
    (let [[property qualifier] arg]
      (case qualifier
        "Self" (vector "HasSelf" property)))) ;is this the only case with two arguments?

(defn translate-restriction-3
  [arg]
    (let [[property qualifier filler] arg]
      (case qualifier
        "some" (vector "SomeValuesFrom" property filler)
        "only" (vector "AllValuesFrom" property filler)
        "value" (vector "HasValue" property filler)
        ;"Self" (vector "HasSelf" property) 
        ;the 'filler' for unqualified cardinality restrictions is a cardinality
        "max" (vector "MaxCardinality" property filler)
        "min" (vector "MinCardinality" property filler)
        "exactly" (vector "ExactCardinality" property filler)
        arg)))

(defn translate-restriction-4
  [arg]
  (let [[property qualifier cardinality filler] arg]
    (case qualifier
      "max" (vector "MaxQualifiedCardinality" property cardinality filler)
      "min" (vector "MinQualifiedCardinality" property cardinality filler)
      "exactly" (vector "ExactQualifiedCardinality" property cardinality filler)
      arg)))

;todo other restrictions
(defn translate-restriction
  [arg]
  (cond
    (= (count arg) 2) (translate-restriction-2 arg)
    (= (count arg) 3) (translate-restriction-3 arg)
    (= (count arg) 4) (translate-restriction-4 arg))) ;qualified cardinality restrictions 


(def ofn-transducer
  {:S (fn [x] x)
   :Description (fn [& x] (translate-description x))
   :Conjunction (fn [& x] (translate-conjunction x))
   :Primary (fn [& x] (translate-primary x))
   :classIRI (fn [x] x)
   :objectPropertyIRI (fn [x] x)
   :Restriction (fn [& x] (translate-restriction x))
   :InverseObjectProperty (fn [x] (vector "InverseOf" x))
   :ObjectPropertyExpression (fn [x] x)
   :Literal (fn [x] x)
   :Individual (fn [x] x)
   :NonNegativeInteger (fn [x] x)
   :PositiveInteger (fn [x] x)


   
   })
