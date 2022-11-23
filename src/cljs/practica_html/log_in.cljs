(ns practica-html.log-in
  (:require
   [practica-html.events :refer [http-request]]
   [reagent.core :as r]))

(defn log-in-view []
  (r/with-let [usuario (r/atom "") contraseña (r/atom "") estado (r/atom {})]
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
       [:input {:type "text"
                :value @usuario
                :on-change #(reset! usuario (-> % .-target .-value))}]
       [:input {:type "password"
                :value @contraseña
                :on-change #(reset! contraseña (-> % .-target .-value))}]
       [:button {:on-click (fn []
                             (swap! estado (fn [estado-previo]
                                             (dissoc estado-previo :ejemplo-1)))
                             (http-request {:method     :post
                                            :uri        "/log-in"
                                            :params     {:user @usuario :pass @contraseña} ;; <- parámetros que irán en el cuerpo de POST
                                            :on-success on-success-fn
                                            :on-failure on-failure-fn}))}
        "Log In"]
       (when-let [valor-éxito (:success (:ejemplo-1 @estado))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo Éxito :D"]
          [:span (str valor-éxito)]])

       (when-let [valor-error (:failure (:ejemplo-1 @estado))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo un Error D:"]
          [:span (str valor-error)]])])))