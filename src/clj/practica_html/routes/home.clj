(ns practica-html.routes.home
  (:require
   [practica-html.layout :as layout]
   [practica-html.middleware :as middleware]
   [ring.util.http-response :as response]
   [ring.util.response]))

(def users [{:user "cheo" :pass "1234"}, {:user "derek" :pass "ffff"}])

(def team [{:name "Eliseo Ortiz Montaño" :img "https://cdn.lorem.space/images/game/.cache/150x220/re-village.jpg"},
           {:name "Derek Almanza Infante" :img "https://cdn.lorem.space/images/game/.cache/150x220/the-walking-dead.jpg"},
           {:name "Fernando" :img "https://cdn.lorem.space/images/game/.cache/150x220/dark-souls.jpg"}])

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn home-page [request]
  (layout/render request "home.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}

   ["/" {:get home-page}]
   ["/mi-end-point" {:get  (fn [_]
                             (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
                     :post (fn [_]
                             (response/ok {:respuesta "Respuesta OK (200)"}))}]
   ["/duplica-numero" {:post (fn [params]
                               (response/ok {:doble (* (:num (:body-params params))
                                                       2)}))}]
   ["/suma" {:post (fn [params]
                     (let [req (:body-params params) a (:a req) b (:b req)]
                       (if (or (not (number? a)) (not (number? b)))
                         (response/bad-request {:res "Ambos parametros deben de ser numeros"})
                         (response/ok {:res (* a b)}))))}]
   ["/log-in" {:post (fn [params]
                       (let [req (:body-params params)]
                         (if (in? users req)
                           (response/ok {:session :ok})
                           (response/bad-request {:res "Datos incorrectos"}))))}]
   ["/integrantes-de-equipo" {:get (fn [_]
                                     (response/ok {:team team}))
                              :post (fn [_]
                                      (response/conflict {:respuesta "Respuesta de Conflict (409)"}))}]])
