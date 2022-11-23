(ns practica-html.integrantes
  (:require
   [practica-html.events :refer [http-request]]
   [reagent.core :as r]))

(defn integrante [integrante]
  (let [nombre (:name integrante) imagen (:img integrante)]
    [:div
     {:style {:border "1px solid black" :border-radius "10px"
              :margin "5px" :padding "5px"}}
     [:h1 (str nombre)]
     [:img {:src imagen}]]))

(defn integrantes []
  (r/with-let [estado (r/atom {})]
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
                             (http-request {:method     :get
                                            :uri        "/integrantes-de-equipo"
                                            :on-success on-success-fn
                                            :on-failure on-failure-fn}))}
        "Obtener integrantes"]
       (when-let [valor-éxito (:success (:ejemplo-1 @estado))]
         [:div {:style {:margin "12px" :display "flex" :flex-direction "column"}}
          (map integrante (:team valor-éxito))])

       (when-let [valor-error (:failure (:ejemplo-1 @estado))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo un Error D:"]
          [:span (str valor-error)]])])))