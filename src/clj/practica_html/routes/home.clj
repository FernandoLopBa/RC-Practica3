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
                 middleware/wrap-formats]}

   ["/" {:get home-page}]

   ["/calculadora" {:post (fn [params]
                            (let [req (:body-params params) valor1 (:valor1 req) valor2 (:valor2 req) op (:op req)]
                              (if (or (not (number? valor1)) (not (number? valor2)))
                                (response/bad-request {:res "Ambos parametros deben de ser numeros"})
                                (response/ok {:res (case op "+" (+ valor1 valor2)
                                                         "-" (- valor1 valor2)
                                                         "*" (* valor1 valor2)
                                                         "/" (/ valor1 valor2))}))))}]
   ["/patch" {:patch (fn [_]
                       (response/ok {:res "Patch correcto :)"}))
              :put (fn [_]
                     (response/conflict {:res "Respuesta de Conflict (409)"}))
              :get (fn [_]
                     (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
              :post (fn [_]
                      (response/conflict {:respuesta "Respuesta de Conflict (409)"}))}]

   ["/mi-end-point" {:get  (fn [_]
                             (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
                     :post (fn [_]
                             (response/ok {:respuesta "Respuesta OK (200)"}))}]
   ["/duplica-numero" {:post (fn [params]
                               (response/ok {:doble (* (:num (:body-params params))
                                                       2)}))}]])
