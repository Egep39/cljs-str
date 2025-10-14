(ns borkdude.cljs-str
  (:refer-clojure :exclude [str])
  (:require [cljs.analyzer]
            [clojure.core :as core]
            [clojure.set]
            [clojure.string :as string]))

(core/defn- compatible? [inferred-tag allowed-tags]
  (if (set? inferred-tag)
    (clojure.set/subset? inferred-tag allowed-tags)
    (contains? allowed-tags inferred-tag)))

(core/defn- string-expr [e]
  (vary-meta e assoc :tag 'string))

(core/defn- typed-expr? [env form allowed-tags]
  (compatible? (cljs.analyzer/infer-tag env
                                        (cljs.analyzer/no-warn (cljs.analyzer/analyze env form)))
               allowed-tags))

(core/defn- compile-time-constant? [x]
  (core/or
   (core/string? x)
   (core/keyword? x)
   (core/boolean? x)
   (core/number? x)))

;; TODO: should probably be a compiler pass to avoid the code duplication
(core/defmacro str
  [& xs]
  (core/let [interpolate (core/fn [x]
                           (core/cond
                             (typed-expr? &env x '#{clj-nil})
                             nil
                             (compile-time-constant? x)
                             ["+~{}" x]
                             :else
                             ;; Note: can't assume non-nil despite tag here, so we go through str 1-arity
                             ["+~{}" (list `?? x)]
                             #_["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(~{})" x]))
             strs+args (keep interpolate xs)
             strs (string/join (map first strs+args))
             args (map second strs+args)]
    (string-expr (list* 'js* (core/str "(\"\"" strs ")") args))))

