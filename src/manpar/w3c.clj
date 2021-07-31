(ns manpar.w3c
  (:require [instaparse.core :as insta]
            [cheshire.core :as cs])
  (:gen-class))

;This follows the Grammar for Manchester Syntax (but we cannot parse IRIs with the symbols '(' or ')' )
(def parser
  (insta/parser
   "S = Description 
    Description = Conjunction <' or '> Conjunction { <' or '> Conjunction }
                | Conjunction
    Conjunction = classIRI ' that ' [ ' not ' ] Restriction { ' and ' [ 'not ' ] Restriction }
                | Primary <' and '>  Primary { <' and '> Primary }
                | Primary
    <Primary> = [ 'not ' ] ( Restriction | atomic )

    Restriction = ObjectPropertyExpression <' some '> Primary 
                | ObjectPropertyExpression <' only '> Primary
                | ObjectPropertyExpression <' min '> NonNegativeInteger [ Primary ]
                | ObjectPropertyExpression <' max '> NonNegativeInteger [ Primary ]
                | ObjectPropertyExpression <' exactly '> NonNegativeInteger [ Primary ]
    <atomic> = classIRI
           | <'('> Description <')'>
    NonNegativeInteger = Zero | PositiveInteger
    PositiveInteger = NonZero { digit }
    digit = Zero | NonZero
    Zero = '0'
    NonZero = '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' 

    classIRI = IRI
    objectPropertyIRI = IRI
    <IRI> = #'[^ ()\\s]*'
    ObjectProperty = #'\\S*'
    ObjectPropertyExpression = objectPropertyIRI 
                             | InverseObjectProperty
    InverseObjectProperty = ' inverse ' objectPropertyIRI
    "))
