(ns borkdude.cljs-str-test
  (:refer-clojure :exclude [str])
  (:require [borkdude.cljs-str :refer [str]]))

(def edge-case-obj #js {:valueOf (fn [] "dude") :toString (fn [] "string")})

(defn str-test []
  (let [f1 (fn [x y] (str x 1 2 nil edge-case-obj (+ 1 2 3) true false "multi

line string with `backticks`" y))
        f2 (fn [x y] (clojure.core/str x 1 2 nil edge-case-obj (+ 1 2 3) true false "multi

line string with `backticks`" y))
        f3 (fn [] (apply str [1 2 3]))
        f4 (fn [] (apply clojure.core/str [1 2 3]))]
    (js/console.log "f1" (str f1))
    (js/console.log "f2" (str f2))
    (js/console.log "f3" (str f3))
    (js/console.log "f4" (str f4))
    (prn (f1 1 :foo))
    (prn (f2 1 :foo))
    (simple-benchmark [] (f1 1 :foo) 100000000)
    (simple-benchmark [] (f2 1 :foo) 100000000)
    (simple-benchmark [] (f3) 100000000)
    (simple-benchmark [] (f4) 100000000))
  (prn (apply str [1 2 3])))

(defn init []
  (str-test))
