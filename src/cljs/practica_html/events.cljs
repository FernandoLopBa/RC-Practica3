(ns practica-html.events
  (:require
   [ajax.core :as ajax]
   [re-frame.core :as rf]
   [reitit.frontend.controllers :as rfc]
   [reitit.frontend.easy :as rfe]))

;;dispatchers

(rf/reg-event-db
 :common/navigate
 (fn [db [_ match]]
   (let [old-match (:common/route db)
         new-match (assoc match :controllers
                                (rfc/apply-controllers (:controllers old-match) match))]
     (assoc db :common/route new-match))))

(rf/reg-fx
 :common/navigate-fx!
 (fn [[k & [params query]]]
   (rfe/push-state k params query)))

(rf/reg-event-fx
 :common/navigate!
 (fn [_ [_ url-key params query]]
   {:common/navigate-fx! [url-key params query]}))


;;subscriptions

(rf/reg-sub
 :common/route
 (fn [db _]
   (-> db :common/route)))

(rf/reg-sub
 :common/page-id
 :<- [:common/route]
 (fn [route _]
   (-> route :data :name)))

(rf/reg-sub
 :common/page
 :<- [:common/route]
 (fn [route _]
   (-> route :data :view)))

(rf/reg-sub
 :docs
 (fn [db _]
   (:docs db)))

(rf/reg-sub
 :common/error
 (fn [db _]
   (:common/error db)))

(rf/reg-event-fx
 :call-back-end
 (fn [_ [_ args]]
   {:http-xhrio {:method          (:method args)
                 :uri             (:uri args)
                 :params          (:params args)
                 :mode "no-cors"
                 :format          (ajax/transit-request-format)
                 :response-format (ajax/transit-response-format)
                 :on-success      [:success (:on-success args)]
                 :on-failure      [:error (:on-failure args)]}}))

(rf/reg-event-fx
 :success
 (fn [_ [_ fn response]]
   (fn response)
   nil))

(rf/reg-event-fx
 :error
 (fn [_ [_ fn response]]
   (fn response)
   nil))

(defn http-request [args]
  (rf/dispatch [:call-back-end args]))
