# CLJS str

A more efficient alternative for CLJS `str` that emits compile time optimized template strings in JS.

## Usage:

``` clojure
(ns borkdude.cljs-str
  (:refer-clojure :exclude [str])
  (:require [borkdude.cljs-str :refer [str])))

(str 1 2 3)
```

## What does this library do?

The CLJS `str` that comes with this library:

- Emits less code
- Emits code that can be optimized by JavaScript engines (~280x faster as shown in the below benchmark):

To illustrate what it emits:

For `(str 1 2 nil (+ 1 2 3))` it spits out a JS template string:

``` javascript
`12${(((1) + (2)) + (3)) ?? ''}`
```

whereas CLJS emits:

``` javascript
[cljs.core.str.cljs$core$IFn$_invoke$arity$1((1)),cljs.core.str.cljs$core$IFn$_invoke$arity$1((2)),null,cljs.core.str.cljs$core$IFn$_invoke$arity$1((((1) + (2)) + (3)))].join('')
```

This test illustrates the gain in performance:

``` javascript
(deftest str-test
  (let [f1 (fn [] (str 1 2 nil (+ 1 2 3)))
        f2 (fn [] (clojure.core/str 1 2 nil (+ 1 2 3)))]
    (js/console.log "f1" (str f1))
    (js/console.log "f2" (str f2))
    (simple-benchmark [] (f1) 100000000)
    (simple-benchmark [] (f2) 100000000)))
```

``` javascript
f1 function (){
return `12${(((1) + (2)) + (3)) ?? ''}`;
}
f2 function (){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1((1)),cljs.core.str.cljs$core$IFn$_invoke$arity$1((2)),null,cljs.core.str.cljs$core$IFn$_invoke$arity$1((((1) + (2)) + (3)))].join('');
}
[], (f1), 100000000 runs, 31 msecs
[], (f2), 100000000 runs, 8982 msecs
```

When using `str` at runtime, e.g. in `(apply str [1 2 3])`,
`borkdude.cljs-str/str` falls back on `cljs.string/join` which is just as fast
as when using `str` with `apply`.

## License

MIT
