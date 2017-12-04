# leontalbot/pull

A tiny Clojure library to manipulate plain datastructures a la Datomic Pull API or Om next queries. 

## Installation
Add the following dependency to your `project.clj` or `build.boot` file

[![clojars project](http://clojars.org/leontalbot/pull/latest-version.svg)](http://clojars.org/leontalbot/pull

## Usage

```clojure
user=> (require '[leontalbot.pull.core :as pull])
nil

user=> (def data {:test {:a "a" :b [{:b1 "b1"} {:b1 "b1" :b2 "b2"}] :c {:c1 "c1" :c2 "c2"} :d #{"d"} :e [1 2 3]}})
#'user/data
```

### `get`

`get` is very similar to `clojure.core/get-in`...

```clojure
user=> (pull/get data [:test])
{:a "a", :b [{:b1 "b1"} {:b1 "b1", :b2 "b2"}], :c {:c1 "c1", :c2 "c2"}, :d #{"d"}, :e [1 2 3]}

user=> (pull/get data [:test :c])
{:c1 "c1", :c2 "c2"}
```
...except when the get process encounters a vector, it maps the the next selector key to every maps inside the collection...

```clojure
user=> (pull/get data [:test :b])
[{:b1 "b1"} {:b1 "b1", :b2 "b2"}]

user=> (pull/get data [:test :b :b1])
("b1" "b1")
```

### `pull`
`pull` is similar to `clojure.core/select-keys`...

```clojure
user=> (pull/pull data [:test])
{:test {:a "a", :b [{:b1 "b1"} {:b1 "b1", :b2 "b2"}], :c {:c1 "c1", :c2 "c2"}, :d #{"d"}, :e [1 2 3]}}
```

...except it can handle nested selection with the hashmap syntax below:

```clojure
user=> (pull/pull data [{:test [:a :b]}])
{:test {:a "a", :b [{:b1 "b1"} {:b1 "b1", :b2 "b2"}]}}

user=> (pull/pull data [{:test [:a {:b [:b1]}]}])
{:test {:a "a", :b ({:b1 "b1"} {:b1 "b1"})}}

```

`pull` also has a 3-arity version, where it accepts both `get` and `pull` queries, in that order. This allows to "get-in" the datastructure before pulling parts from it:

```clojure
user=> (pull/pull data [:test] [:a :b])
{:a "a", :b [{:b1 "b1"} {:b1 "b1", :b2 "b2"}]}

user=> (pull/pull data [:test] [:a {:b [:b1]}])
{:a "a", :b ({:b1 "b1"} {:b1 "b1"})}
```

## TODO (if necessary)

What can we do from there?

```clojure
;; select all except a key?
(pull/pull data {:test [:* :e]}
;; this dissoc :e at :test

;; links?
(def test-2 {:group [[:people :id 1] [:people :id 3]] 
             :people [{:id 1 :name "Jim" :friends [[:people :id 2]]},
                      {:id 2 :name "Paul" :friends [[:people :id 1]]}
                      {:id 3 :name "Ryan" :friends [[:people :id 5]]}]})

(pull/pull test-2 [{:group [:name]}])
;; => {:group [{:name "Jim"} {:name "Ryan"}]}

```

## References
* [awkay/om_tutorial.D_Queries](https://awkay.github.io/om-tutorial/#!/om_tutorial.D_Queries)
* [awkay/om_tutorial.E_UI_Queries_and_State](https://awkay.github.io/om-tutorial/#!/om_tutorial.E_UI_Queries_and_State)
* [day-of-datomic/tutorial/pull](https://github.com/Datomic/day-of-datomic/blob/master/tutorial/pull.clj)
* [day-of-datomic/tutorial/pull_recursion](https://github.com/Datomic/day-of-datomic/blob/master/tutorial/pull_recursion.clj)

## License

Copyright © 2017 Leon Talbot

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
