(ns practica-html.delete
  (:require
   [practica-html.events :refer [http-request]]
   [reagent.core :as r]))

(defn delete-view []
  (r/with-let [estado (r/atom {})]
    (let [on-success-fn (fn [http-response]
                        ;; Función ejecutada cuando hay un código de éxito
                          (prn "Éxito :D " http-response)
                          (swap! estado
                                 (fn [estado-previo]
                                   (assoc-in estado-previo [:ejemplo-1 :success] http-response))))
          on-failure-fn (fn [http-response]
                        ;; Función ejecutada cuando hay un código de error
                          (println "Error d:")
                          (swap! estado
                                 (fn [estado-previo]
                                   (assoc-in estado-previo [:ejemplo-1 :failure] http-response))))]
      [:div
       [:button {:on-click (fn []
                             (swap! estado (fn [estado-previo]
                                             (dissoc estado-previo :ejemplo-1)))
                             (http-request {:method     :delete
                                            :uri        "/delete"
                                            :params     {:a 2 :b 20} ;; <- parámetros que irán en el cuerpo de POST
                                            :on-success on-success-fn
                                            :on-failure on-failure-fn}))}
        "Delete"]
       (when-let [valor-éxito (:success (:ejemplo-1 @estado))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo Éxito :d"]
          [:span (str valor-éxito)]])

       (when-let [valor-error (:failure (:ejemplo-1 @estado))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo un Error c:"]
          [:span (str valor-error)]])])))
