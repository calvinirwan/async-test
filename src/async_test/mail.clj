(ns async-test.mail
  (:gen-class)
  (:require [clj-mailgun.core :as m]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]
            [clj-http.client :as client]
            [async-test.config :refer [mailgun mailgun2
                                       mail1 mail2 mail3]]))

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

