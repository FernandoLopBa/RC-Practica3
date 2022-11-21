(ns practica-html.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [practica-html.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[practica-html started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[practica-html has shut down successfully]=-"))
   :middleware wrap-dev})
