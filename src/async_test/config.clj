(ns async-test.config)

(def config (read-string (slurp "config.edn")))
(def mail (read-string (slurp "mail.edn")))

(def mailgun (config :mailgun))
(def mailgun2 (config :mailgun2))
(def mailtrap (config :mailtrap))

(def mail1 (mail :mail1))
(def mail2 (mail :mail2))
(def mail3 (mail :mail3))
(def mail-cred (mail :mail-cred))
