(ns practica-html.core
  (:require
    [day8.re-frame.http-fx]
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [practica-html.ajax :as ajax]
    [practica-html.events]
    [reitit.core :as reitit]
    [reitit.frontend.easy :as rfe]
    [practica-html.view :as my-view])
  (:import goog.History))

(defn navbar []
  [:nav.navbar.is-info>div.container
   [:div.navbar-brand
    [:a.navbar-item {:href "/" :style {:font-weight :bold}} "Práctica HTML"]]])

(defn home-page []
  [:section.section>div.container>div.content
   [my-view/page]])

(defn page []
  (if-let [page @(rf/subscribe [:common/page])]
    [:div
     [navbar]
     [page]]))

(defn navigate! [match _]
  (rf/dispatch [:common/navigate match]))

(def router
  (reitit/router
   [["/" {:name :home
          :view #'home-page}]]))

(defn start-router! []
  (rfe/start! router navigate! {}))

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (rdom/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (start-router!)
  (ajax/load-interceptors!)
  (mount-components))
