(ns practica-html.routes.home
  (:require
   [clojure.string :as str]
   [practica-html.layout :as layout]
   [practica-html.middleware :as middleware]
   [ring.util.http-response :as response]
   [ring.util.response]))

(def users [{:user "cheo" :pass "1234"}, {:user "derek" :pass "ffff"}, {:user "flb" :pass "password"}])

(def team [{:name "Eliseo Ortiz Montaño" :img "https://cdn.lorem.space/images/game/.cache/150x220/re-village.jpg"},
           {:name "Derek Almanza Infante" :img "https://cdn.lorem.space/images/game/.cache/150x220/the-walking-dead.jpg"},
           {:name "Fernando Lopez Balcazar" :img "https://cdn.lorem.space/images/game/.cache/150x220/dark-souls.jpg"}])

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
                                                       2)}))}]
   ["/suma" {:post (fn [params]
                     (let [req (:body-params params) a (:a req) b (:b req)]
                       (if (or (not (number? a)) (not (number? b)))
                         (response/bad-request {:res "Ambos parametros deben de ser numeros"})
                         (response/ok {:res (+ a b)}))))}]
   ["/suma-general" {:post (fn [params]
                             (try
                               (response/ok {:respuesta (apply + (into [] map #(Integer/parseInt %) (str/split params #" ")))})
                               (catch NumberFormatException a
                                 (response/bad-request "mala entrada")
                                 )))
   ["/secure-password" {:post (fn [param] 
                                (let [req (:body-params param) password (:password req)]
                                  (if (< (count (str password) 12))
                                    (response/bad-request {:respuesta "La longitud debe ser mayor a 12"})
                                    (if (not (some #(Character/isUpperCase %) password))
                                      (response/bad-request {:respuesta "La contraseña debe contener al menos una letra mayuscula"})
                                      (if (not (some #(Character/isLowerCase %) password))
                                        (response/bad-request {:respuesta "La contraseña debe contener al menos una letra minúscula"})
                                        (if (not (some #(Character/isDigit %) password))
                                          (response/bad-request {:respuesta "La contraseña debe contener al menos un número"})
                                          (response/ok {:respuesta "La contraseña es segura de acuerdo a https://www.muhlenberg.edu/offices/oit/about/policies_procedures/strong-passwords.html"}))))))
                                )}]
  ["/log-in" {:post (fn [params]
                       (let [req (:body-params params)]
                         (if (in? users req)
                           (response/ok {:session :ok})
                           (response/bad-request {:res "Datos incorrectos"}))))}]
   ["/integrantes-de-equipo" {:get (fn [_]
                                     (response/ok {:team team}))
                              :post (fn [_]
                                      (response/conflict {:respuesta "Respuesta de Conflict (409)"}))}]
   ["/put" {:put (fn [_]
                   (response/ok {:res "Put correcto :)"}))
            :get (fn [_]
                   (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
            :post (fn [_]
                    (response/conflict {:respuesta "Respuesta de Conflict (409)"}))}]
   ["/delete" {:delete (fn [_]
                   (response/ok {:res "Delete correcto :)"}))
            :put (fn [_]
                   (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
            :get (fn [_]
                   (response/conflict {:respuesta "Respuesta de Conflict (409)"}))
            :post (fn [_]
                    (response/conflict {:respuesta "Respuesta de Conflict (409)"}))}]])
