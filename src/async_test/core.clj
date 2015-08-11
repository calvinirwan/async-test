(ns async-test.core
  (:gen-class)
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! take! put!
                     go go-loop chan buffer close!
                     thread alts! alts!! timeout]]
            [clojure.set :as s]
            [clj-mailgun.core :as m]
            [cheshire.core :as c]
            [async-test.mail :refer [valid-mail? send-mail]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def a-chan (chan))
(def b-chan (chan))

(defn add-string
  [a-chan b-chan]
  (loop [a a-chan b b-chan]
    (go (>! b (inc (<! a))))
    (go (println (<! b)))
    (recur a b)))

(def config (read-string (slurp "config.edn")))
(def mail-chan (chan 10))
(def valid-mail-chan (chan 5))
(def invalid-mail-chan (chan 5))

#_(defn valid-mail?
  [mail]
  (< 5 (count mail)))

(defn mail-allocate
  [mail-chan]
  (go (let [mail (<! mail-chan)]
        (if (valid-mail? mail)
          (>! valid-mail-chan (str mail "@gmail.com"))
          (>! invalid-mail-chan (str mail "@xhamsters.com"))))))

(defn infinite-mail-check
  []
  (go-loop []
    #_(>! vmc (str (<! mail-chan) "@gmial.cu"))
    #_(mail-allocate mail-chan)
    (let [mail (<! mail-chan)]
      (if (valid-mail? mail)
        (>! valid-mail-chan (str mail "@gmail.com"))
        (>! invalid-mail-chan (str mail "@xhamsters.com")))
        (recur))))

(defn infinite-mail-send
  []
  (go-loop []
    (let [vmc valid-mail-chan]
      (println (<! vmc))
      (recur))))

(defn tiger []
  (infinite-mail-check))

(defn eagle []
  (infinite-mail-send))

