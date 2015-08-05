(ns async-test.core
  (:gen-class)
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! take! put!
                     go chan buffer close!
                     thread alts! alts!! timeout]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def echo-chan (chan))
(go (println (<! echo-chan)))
(>!! echo-chan "ketchup")

(def c (chan))
(take! c (fn [v] (println v)))

(defn takep [c]
  (let [p (promise)]
    (take! c (fn [v] (deliver p v)))
    @p))

(def f (future (Thread/sleep 10000) (println "done") 100))

(defn mail-machine
  []
  "lol")
