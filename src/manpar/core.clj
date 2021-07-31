(ns manpar.core
  (:require [instaparse.core :as insta] 
            [manpar.prototype :as prototype]
            [manpar.w3c :as w3c]
            [cheshire.core :as cs])
  (:gen-class))

(defn -main
  "Manual Testing"
  [& args]
  (let [cl1 "ex:pExactQualifiedCardinality max 2 ex:exactQualifiedCardinalityFiller"
        cl2 "ex:pUniversal only ex:universalFiller"
        cl3 "ex:pHasSelf Self" 
        cl4 "ex:pMaxQualifiedCardinality max 1 ex:maxCardinalityFiller"
        cl5 "ex:pMinCardinality min 1 owl:Thing"
        cl6 "ex:pMaxCardinality max 1 owl:Thing"
        cl7 "ex:pMinQualifiedCardinality min 1 ex:minCardinalityFiller"
        cl8 "(not (ex:complement))" 
        cl9 "ex:pHasValue value ex:a1"
        cl10 "ex:pExactCardinality max 2 owl:Thing"
        cl11 "ex:pExistential some ex:existentialFiller"
        cl12 "ex:u1 or ex:u2 or ex:u3"
        
        obi1 "obo:OBI_0001875 and (obo:OBI_0000643 some (obo:CL_0000000 and (not (obo:BFO_0000051 some obo:OBI_1110132)) and (not (obo:BFO_0000051 some obo:PR_000001004))))"
        obi2 "obo:OBI_0002075 and (obo:OBI_0000299 some (obo:IAO_0000136 some (obo:OBI_0001503 and (not (obo:OBI_1110060 some obo:GO_0019882)))))"
        obi3 "obo:IAO_0000109 and (not (obo:IAO_0000416))"
        obi4 "obo:BFO_0000051 some (obo:IAO_0000027 and (obo:IAO_0000136 some obo:IAO_0000096))"
        obi5 "obo:OBI_0000299 some (obo:IAO_0000027 and (obo:IAO_0000136 some (obo:BFO_0000040 and (obo:RO_0000087 some obo:OBI_0000067))))" 
        obi6 "(obo:OBI_0000293 some (obo:IAO_0000010 or obo:IAO_0000096)) and (obo:OBI_0000299 some obo:IAO_0000010)"
        
    ;axiom1 EquivalentTo: inverse (obo:OBI_0000295) 

        axiom1 "Class: obo:IAO_0000065 SubClassOf: obo:IAO_0000064" 
        axiom2 "Class: obo:OBI_0000070 SubClassOf: obo:OBI_0000299 some (obo:IAO_0000027 and (obo:IAO_0000136 some (obo:BFO_0000040 and (obo:RO_0000087 some obo:OBI_0000067))))" 
        axiom3 "Class: obo:APOLLO_SV_00000008 EquivalentTo: obo:OBI_0000011 and (obo:OBI_0000299 some (obo:IAO_0000010 or obo:IAO_0000096))" 
        axiom4 "Class: obo:IAO_0000078 EquivalentTo: {obo:IAO_0000002 , obo:IAO_0000120 , obo:IAO_0000121 , obo:IAO_0000122 , obo:IAO_0000123 , obo:IAO_0000124 , obo:IAO_0000125 , obo:IAO_0000423 , obo:IAO_0000428}" 
        axiom5 "Class: obo:OBI_0000070 DisjointWith: obo:OBI_0000339, obo:OBI_0200000, obo:OBI_0600013"
        ]


  (println "")
  (println "Test class expresions")
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl1))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl2))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl3))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl4))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl5))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl6))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl7))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl8))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl9))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl10))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl11))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser cl12)))) 

  (println "")
  (println "Test example expressions taken from obi")

  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser obi1))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser obi2))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser obi3))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser obi4))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser obi5))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser obi6))))

  (println "")
  (println "Test example axioms taken from obi")

  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser axiom1))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser axiom2))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser axiom3))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser axiom4))))
  (println (cs/generate-string (insta/transform prototype/ofn-transducer (prototype/parser axiom5))))


  ))
