(ns async-test.core
  (:gen-class)
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! take! put!
                     go go-loop chan buffer close!
                     thread alts! alts!! timeout]]
            [clojure.set :as s]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; (def echo-chan (chan))
;; (go (println (<! echo-chan)))
;; (>!! echo-chan "ketchup")

;; (def c (chan))
;; (take! c (fn [v] (println v)))

;; (defn takep [c]
;;   (let [p (promise)]
;;     (take! c (fn [v] (deliver p v)))
;;     @p))

;; (def f (future (Thread/sleep 10000) (println "done") 100))

;; (def mail-chan (chan))

;; (defn mail-machine
;;   []
;;   (let [mail-chan (chan)]
;;     (go (while true (mail-validation (<! c))))
;;     (dotimes )))

(def a-chan (chan))
(def b-chan (chan))

(defn add-string
  [a-chan b-chan]
  (loop [a a-chan b b-chan]
    (go (>! b (inc (<! a))))
    (go (println (<! b)))
    (recur a b)))


;; (def tea-chan (chan 10))
;; (>!! tea-chan :tea-1)
;; (<!! tea-chan)
;; (a/close! tea-chan)
;; (a/go-loop []
;;   (println "Thanks for the " (<! tea-chan))
;;   (recur))

(def mail-chan (chan 10))
(def valid-mail-chan (chan 5))
(def invalid-mail-chan (chan 5))

(defn valid-mail?
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
        (>! invalid-mail-chan (str mail "@xhamsters.com"))))
    (println "mail is valid " (<! valid-mail-chan))
    (println "mail is invalid " (<! invalid-mail-chan))
    (recur)))

(defn tiger []
  (infinite-mail-check))
