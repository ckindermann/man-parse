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
    Conjunction = classIRI <WS> 'that' <WS> [ 'not' <WS> ] Restriction { <WS> 'and' <WS> [ 'not' <WS> ] Restriction }
                | Primary <WS> <'and'> <WS>  Primary { <WS> <'and'> <WS> Primary }
                | Primary
    <Primary> = [ 'not' <WS> ] ( Restriction | atomic )

    Restriction = ObjectPropertyExpression <WS> <'some'> <WS> Primary 
                | ObjectPropertyExpression <WS> <'only'> <WS> Primary
                | ObjectPropertyExpression <WS> <'min'> <WS> NonNegativeInteger <WS> [ Primary ]
                | ObjectPropertyExpression <WS> <'max'> <WS> NonNegativeInteger <WS> [ Primary ]
                | ObjectPropertyExpression <WS> <'exactly'> <WS> NonNegativeInteger <WS> [ Primary ]
                | ObjectPropertyExpression <WS> 'Self'
                | ObjectPropertyExpression <WS> 'value' <WS> Literal

    <atomic> = classIRI
           | <'('> Description <')'>

    WS = #'\\s+' 

    NonNegativeInteger = Zero | PositiveInteger
    PositiveInteger = NonZero { digit }
    digit = Zero | NonZero
    Zero = '0'
    NonZero = '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' 

    Literal = #'.*'
    classIRI = IRI
    objectPropertyIRI = IRI
    <IRI> = #'[^ ()\\s]*'
    ObjectProperty = #'\\S*'
    ObjectPropertyExpression = objectPropertyIRI 
                             | InverseObjectProperty
    InverseObjectProperty = ' inverse ' objectPropertyIRI
    "))
