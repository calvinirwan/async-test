(ns async-test.mailtrap
  (:gen-class)
  (:require [clj-mailgun.core :as m]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as c]
            [clj-http.client :as client]))
