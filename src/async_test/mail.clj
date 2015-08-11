(ns async-test.mail
  (:gen-class)
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! take! put!
                     go go-loop chan buffer close!
                     thread alts! alts!! timeout]]
            [clojure.set :as s]
            [clj-mailgun.core :as m]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]))

(def config (read-string (slurp "config.edn")))
(def mail (read-string (slurp "mail.edn")))
(def mail2 (read-string (slurp "mail2.edn")))
(defn validation-mail-gun
  [email-id]
  (let [credentials {:api-key (config :mgun-pub-key)}]
    (m/validate-email credentials email-id)))

(defn valid-mail?
  [email]
  (let [email-id (email :to)
        mail-gun-response (validation-mail-gun email-id)
        status (mail-gun-response :status)
        body (c/parse-string (mail-gun-response :body) true)
        mail-validity (body :is_valid)]
    [email-id status body mail-validity]))

(defn send-mail
  [config email]
  (let [params email
        credentials {:api-key (config :mgun-pri-key) :domain (config :mgun-domain)}]
    (m/send-email credentials params)))
