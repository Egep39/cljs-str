(ns borkdude.cljs-str
  (:refer-clojure :exclude [str])
  (:require [clojure.core :as core]
            [clojure.string :as str]))

(defn constant? [x]
  (or (number? x)
      (keyword? x)
      (string? x)
      (boolean? x)))

(defmacro str [& xs]
  (let [args (map (fn [expr]
                    (cond (nil? expr)
                          [nil nil]
                          (constant? expr)
                          ["+~{}" expr]
                          :else ["+~{}" (list `?? expr)])) xs)]
    `(~'js*
      ~(core/str "''" (str/join (keep first args)))
      ~@(keep second args))))
