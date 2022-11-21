(ns practica-html.view
  (:require [practica-html.events :refer [http-request]]
            [practica-html.integrantes :refer [integrantes]]
            [practica-html.log-in :refer [log-in-view]]
            [practica-html.suma :refer [suma-view]]
            [reagent.core :as r]))

(defn button-custom [estado]
  (let [on-success-fn (fn [http-response]
                        ;; Función ejecutada cuando hay un código de éxito
                        (prn "Éxito :D " http-response)
                        (swap! estado
                               (fn [estado-previo]
                                 (assoc-in estado-previo [:ejemplo-1 :success] http-response))))
        on-failure-fn (fn [http-response]
                        ;; Función ejecutada cuando hay un código de error
                        (println "Error D:")
                        (swap! estado
                               (fn [estado-previo]
                                 (assoc-in estado-previo [:ejemplo-1 :failure] http-response))))]
    [:div
     [:button {:on-click (fn []
                           (swap! estado (fn [estado-previo]
                                           (dissoc estado-previo :ejemplo-1)))
                           (http-request {:method     :post
                                          :uri        "/duplica-numero"
                                          :params     {:num 20} ;; <- parámetros que irán en el cuerpo de POST
                                          :on-success on-success-fn
                                          :on-failure on-failure-fn}))}
      "Mi botón 2"]
     (when-let [valor-éxito (:success (:ejemplo-1 @estado))]
       [:div {:style {:margin "12px"}}
        [:h2 "Hubo Éxito :D"]
        [:span (str valor-éxito)]])

     (when-let [valor-error (:failure (:ejemplo-1 @estado))]
       [:div {:style {:margin "12px"}}
        [:h2 "Hubo un Error D:"]
        [:span (str valor-error)]])]))

(defn page []
  (r/with-let [estado (r/atom {})] ;; <- Estado del componente
    ;; Eliminar este código e implementar el propio:
    [:div
     [:img {:src "/img/warning_clojure.png"}]
     [:div {:style {:display        :flex
                    :flex-direction :column
                    :row-gap        18
                    :margin-top     "10px"}}

      [:span "Esta es la página"]

      [:button {:on-click (fn []
                            (println "Esto se imprime cuando haces click. Abre la consola de JS :)"))}
       "Mi botón 1"]

      [:div {:style {:display       :flex
                     :padding       "12px 24px"
                     :border-radius "6px"
                     :border        "2px solid #9370DB"
                     :column-gap    16}}
       [:input {:on-change (fn [e]
                             (swap! estado (fn [estado-previo]
                                             (assoc-in estado-previo
                                                       [:ejemplo-0 :valor]
                                                       (.-value (.-target e))))))}]
       [:button {:on-click (fn []
                             ;; Puede hacer una llamada al back-end con el valor del input
                             )}
        "Otro botón"]
       [:div {:style {:display     :flex
                      :align-items :center
                      :padding     "8px 12px 4px 12px"
                      :border-radius "4px"
                      :border      "1px solid red"}}
        [:span
         [:span {:style {:font-weight "600"
                         :color       "Salmon"}}
          "Valor del input: "]
         (str  (get-in @estado [:ejemplo-0 :valor]))]]]

      [button-custom estado]
      [suma-view]
      [log-in-view]
      [integrantes]]])) ;; <- Manera de usar un componente propio.
