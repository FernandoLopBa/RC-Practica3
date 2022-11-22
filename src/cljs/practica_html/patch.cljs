(ns practica-html.patch
  (:require
   [practica-html.events :refer [http-request]]
   [reagent.core :as r]))

(defn patch-view []
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
                             (http-request {:method     :patch
                                            :uri        "/patch"
                                            :params     {:valor1 453 :valor2 224} ;; <- parámetros que irán en el cuerpo de POST
                                            :on-success on-success-fn
                                            :on-failure on-failure-fn}))}
        "Probar patch"]
       (when-let [valor-éxito (:success (:ejemplo-1 @res))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo Éxito :D"]
          [:span (str valor-éxito)]])

       (when-let [valor-error (:failure (:ejemplo-1 @res))]
         [:div {:style {:margin "12px"}}
          [:h2 "Hubo un Error D:"]
          [:span (str valor-error)]])])))