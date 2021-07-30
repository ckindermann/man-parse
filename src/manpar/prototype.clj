(ns manpar.prototype
  (:require [instaparse.core :as insta] 
            [cheshire.core :as cs])
  (:gen-class))

(def parser
  (insta/parser
    "S = ClassExpression 
    <ClassExpression> = Primary
                    | <'('> Primary <')'>
   
    <Primary> = Conjunction 
            | Disjunction
            | Restriction
            | Negation
            | ClassIRI
    
    Conjunction = ClassExpression <' and '> ClassExpression { <' and '> ClassExpression }
    Disjunction = ClassExpression <' or '> ClassExpression { <' or '> ClassExpression }
    Negation = <'not '> ClassExpression

    <Restriction> = Existential
                | Universal
                | MinCardinality
                | MaxCardinality
                | ExactCardinality
                | Self
                | IndividualValue
   
    Existential = ObjectPropertyExpression <' some '> ClassExpression 
    Universal = ObjectPropertyExpression <' only '> ClassExpression
    MinCardinality = ObjectPropertyExpression <' min '> NonNegativeInteger [ <' '> ClassExpression ]
    MaxCardinality = ObjectPropertyExpression <' max '> NonNegativeInteger [ <' '> ClassExpression ]
    ExactCardinality = ObjectPropertyExpression <' exactly '> NonNegativeInteger [ <' '> ClassExpression ]
    Self = ObjectPropertyExpression <' Self'>
    IndividualValue = ObjectPropertyExpression <' value '> Individual

    <ObjectPropertyExpression> = objectPropertyIRI 
                             | InverseObjectProperty
    InverseObjectProperty = <' inverse '> objectPropertyIRI

    ClassIRI = IRI
    objectPropertyIRI = IRI
    <Individual> = IRI
    <IRI> = #'[^ ()\\s]*'

    <NonNegativeInteger> = Zero | PositiveInteger
    <PositiveInteger> = NonZero { digit }
    <digit> = Zero | NonZero
    <Zero> = '0'
    <NonZero> = '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' "))

(def ofn-transducer
  { 
  :S (fn [x] x)
  :ClassIRI (fn [x] x)
  :objectPropertyIRI (fn [x] x)
  :Existential (fn [x y] (vector "SomeValuesFrom" x y))
  :Universal (fn [x y] (vector "AllValuesFrom" x y))
  :MinCardinality (fn [& args] (vec (cons "MinCardinality" args)))
  :MaxCardinality (fn [& args] (vec (cons "MaxCardinality" args)))
  :ExactCardinality (fn [& args] (vec (cons "ExactCardinality" args)))
  :Negation (fn [x] (vector "Complement" x))
  :Conjunction (fn [& args] (vec (cons "IntersectionOf" args)))
  :Disjunction (fn [& args] (vec (cons "UnionOf" args)))
  :InverseObjectProperty (fn [x] (vector "InverseOf" x))
  :Self (fn [x] (vector "ObjectHasSelf" x))
  :IndividualValue (fn [x y] (vector "HasValue" x y))
  })

