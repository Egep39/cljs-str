(ns borkdude.cljs-str-test
  (:refer-clojure :exclude [str])
  (:require [borkdude.cljs-str :refer [str]]))

(defn str-test []
  (let [f1 (fn [] (str 1 2 nil (+ 1 2 3) true false))
        f2 (fn [] (clojure.core/str 1 2 nil (+ 1 2 3) true false))
        f3 (fn [] (apply str [1 2 3]))
        f4 (fn [] (apply clojure.core/str [1 2 3]))]
    (js/console.log "f1" (str f1))
    (js/console.log "f2" (str f2))
    (js/console.log "f3" (str f3))
    (js/console.log "f4" (str f4))
    (simple-benchmark [] (f1) 100000000)
    (simple-benchmark [] (f2) 100000000)
    (simple-benchmark [] (f3) 100000000)
    (simple-benchmark [] (f4) 100000000))
  (prn (apply str [1 2 3])))

(defn init []
  (str-test))
