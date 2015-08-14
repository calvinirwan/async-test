(ns async-test.mailtrap
  (:gen-class)
  (:require [clj-mailgun.core :as m]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]
            [clj-http.client :as client]
            [postal.core :refer [send-message] :as p]
            [async-test.config :refer [mailtrap]]))

(def conn
  (let [{:keys [host user_name password authentication port]} mailtrap]
    {:host host
     :user user_name 
     :pass password
     :authentication authentication}))

(defn send-mail
  [email subject body]
  (send-message conn {:from (mailtrap :user_name)
                      :to email
                      :subject subject
                      :body body}))

