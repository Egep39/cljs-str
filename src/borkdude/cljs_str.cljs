(ns borkdude.cljs-str
  (:refer-clojure :exclude [str])
  (:require-macros [borkdude.cljs-str])
  (:require [clojure.string :as str]))

(defn str
  "Runtime version of `str`, only used as fallback when macro isn't invoked."
  [& xs]
  (str/join "" xs))
