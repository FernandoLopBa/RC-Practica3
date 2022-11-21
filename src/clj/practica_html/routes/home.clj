(ns practica-html.routes.home
  (:require
   [practica-html.layout :as layout]
   [practica-html.middleware :as middleware]
   [ring.util.http-response :as response]
   [ring.util.response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats
                 ]}

   ["/" {:get home-page}]
   ["/mi-end-point" {:get  (fn [_]
                             (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
                     :post (fn [_]
                             (response/ok {:respuesta "Respuesta OK (200)"}))}]
   ["/duplica-numero" {:post (fn [params]
                               (response/ok {:doble (* (:num (:body-params params))
                                                       2)}))}]])
