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
                    (cond (constant? expr)
                          [(core/str "+" (if (string? expr)
                                           (pr-str expr)
                                           expr)) nil]
                          (nil? expr)
                          ["" nil]
                          :else ["+~{}" (list `?? expr)])) xs)]
    `(~'js*
      ~(core/str "''" (str/join "" (map first args)))
      ~@(keep second args))))
