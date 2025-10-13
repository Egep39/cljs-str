# CLJS str

A more efficient alternative for CLJS `str` that emits optimizable JS code.

## Usage:

``` clojure
(ns borkdude.cljs-str
  (:refer-clojure :exclude [str])
  (:require [borkdude.cljs-str :refer [str])))

(str 1 2 3)
```

## What does this library do?

The CLJS `str` that comes with this library emits code that can be optimized by
JavaScript engines. In some cases, especially when you use constant values,
e.g. `(str "foo" "bar")`, it can be -280x faster. With mostly variables,
e.g. `(str "foo" x y z)`, this version is still about 4x faster due to avoiding
allocating arrays.

Note that the output of this library is fully compatible with older versions of JS.

This test illustrates the gain in performance:

``` javascript
(defn str-test
  (let [f1 (fn [] (str 1 2 nil (+ 1 2 3)))
        f2 (fn [] (clojure.core/str 1 2 nil (+ 1 2 3)))]
    (simple-benchmark [] (f1) 100000000)
    (simple-benchmark [] (f2) 100000000)))
```

``` javascript
[], (f1), 100000000 runs, 31 msecs
[], (f2), 100000000 runs, 8982 msecs
```

When using `str` at runtime, e.g. in `(apply str [1 2 3])`,
`borkdude.cljs-str/str` falls back on `cljs.string/join` which is just as fast
as when using `str` with `apply`.

## License

MIT
