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
            [cheshire.core :as c]
            [clj-http.client :as client]))

(def config (read-string (slurp "config.edn")))
(def mail (read-string (slurp "mail.edn")))

(def mailgun (config :mailgun))
(def mailgun2 (config :mailgun2))
(def mailtrap (config :mailtrap))

(def mail1 (mail :mail1))
(def mail2 (mail :mail2))
(def mail3 (mail :mail3))

(defn validation-mail-gun
  [mailgun-config email-id]
  (let [credentials {:api-key (mailgun-config :pub-key)}]
    (m/validate-email credentials email-id)))

(defn valid-mail?
  [mailgun-config email]
  (let [email-id (email :to)
        mail-gun-response (validation-mail-gun mailgun-config email-id)
        {:keys [status body]} mail-gun-response
        body (c/parse-string body true)
        mail-validity (body :is_valid)]
    mail-validity))

(defn lol-mail?
  [mailgun-config email]
  (let [email-id (email :to)
        mail-gun-response (validation-mail-gun mailgun-config email-id)
        ;{:keys [status body]} mail-gun-response
        ;body (c/parse-string body true)
        ;mail-validity (body :is_valid)
        ]
    [email-id  (str mail-gun-response)]))

(defn send-mail
  [mailgun-config email]
  (let [params email
        credentials {:api-key (mailgun-config :key) :domain (mailgun-config :domain)}]
    (m/send-email credentials params)))


(defn mailtrap-url-maker
  [api-url url]
  (str api-url url))

(defn mailtrap-api
  "send get request to mailtrap api, fill the url with the api url"
  [mailtrap-config url]
  (let [{:keys [api_token api]} mailtrap-config]
    (client/get (mailtrap-url-maker api url)
                {:headers {:Api-token api_token}})))

(defn mailtrap-api-post
  "send post request to mailtrap api, fill the url with the api url"
  [mailtrap-config url]
  (let [{:keys [api_token api]} mailtrap-config]
    (client/get (mailtrap-url-maker api url)
                {:headers {:Api-token api_token}})))

#_(defn mailtrap-send
  []
  (client/post ))

