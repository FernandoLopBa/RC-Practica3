(ns practica-html.calculadora
  (:require
   [practica-html.events :refer [http-request]]
   [reagent.core :as r]))

(defn calculadora-view []
  (r/with-let [res (r/atom {})]
    (let [on-success-fn (fn [http-response]
                        ;; Función ejecutada cuando hay un código de éxito
                          (prn "Success " http-response)
                          (swap! res
                                 (fn [res-previo]
                                   (assoc-in res-previo [:ejemplo-1 :success] http-response))))
          on-failure-fn (fn [http-response]
                        ;; Función ejecutada cuando hay un código de error
                          (println "Error :'v")
                          (swap! res
                                 (fn [res-previo]
                                   (assoc-in res-previo [:ejemplo-1 :failure] http-response))))]
      [:div
       [:button {:on-click (fn []
                             (swap! res (fn [res-previo]
                                          (dissoc res-previo :ejemplo-1)))
                             (http-request {:method     :post
                                            :uri        "/calculadora"
                                            :params     {:valor1 2 :valor2 20 :op "-"} ;; <- parámetros que irán en el cuerpo de POST
                                            :on-success on-success-fn
                                            :on-failure on-failure-fn}))}
        "Calculadora de dos números"]
       (when-let [valor-éxito (:success (:ejemplo-1 @res))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo Éxito :D"]
          [:span (str valor-éxito)]])

       (when-let [valor-error (:failure (:ejemplo-1 @res))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo un Error D:"]
          [:span (str valor-error)]])])))