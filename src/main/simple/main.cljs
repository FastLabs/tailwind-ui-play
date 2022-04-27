(ns simple.main
  (:require [reagent.dom :as dom]
            [re-frame.core :as rf]
            [reitit.core :as route]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend :refer [router]]
            [component.combobox :refer [Combobox]]
            ["@heroicons/react/outline" :refer [InboxIcon]]
            ["@heroicons/react/solid" :as icon-solid]))

(defn home-page []
  [:div "Home page"])

(defn create-rfc-view []
  [:div "create new rfc"])

(defn list-rfc []
  [:div "List rfc"])

(def routes
  ["/"
   ["" {:name     ::home
        :nav-icon InboxIcon
        :nav-text "Home"
        :view     home-page}]
   ["create-rfc" {:name     ::create-rfc
                  :nav-text "Create Rfc"
                  :nav-icon InboxIcon
                  :view     create-rfc-view}]
   ["list-rfc" {:name     ::list-rfc
                :nav-text "List RFC"
                :nav-icon InboxIcon
                :view     list-rfc}]])

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [::navigated new-match])))

(def app-router
  (router routes))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start!
    app-router
    on-navigate
    {:use-fragment true}))

;TODO: look at toggle css function i had
(defn app-header []
  [:header {:class-name "flex-shrink-0 relative h-16 bg-white flex items-center"}
   [:div {:class-name "absolute inset-y-0 left-0 lg:static lg:flex-shrink-0"}]
   [:div {:class-name ""}]])


(defn app-sidebar [router current-route]
   [:nav {:aria-label "Application Sidebar"
          :class-name "hidden lg:block lg:flex-shrink-0 lg:bg-gray-800 lg:overflow-y-auto"}
    [:div {:class-name "relative w-20 flex flex-col p-3 space-y-3"}
     (for [route-name (route/route-names router)
           :let [route      (route/match-by-name router route-name)
                 route-data (:data route)]]
        [:a {:href       (rfe/href (-> route-data :name))
             :key (-> route-data  :name (name))
             :class-name (str (if (= (-> current-route :data :name)
                                     route-name) "bg-gray-900 text-white" "text-gray-400 hover:bg-gray-700") " "
                              "flex-shrink-0 inline-flex items-center justify-center h-14 w-14 rounded-lg")}
         [:span {:class-name "sr-only"} (-> route-data :nav-text)]
         [:> (-> route-data :nav-icon) {:class-name  "h-6 w-6"
                                        :aria-hidden true}]])]])

(defn app-view [current-route]
  [:main {:class-name "min-w-0 flex-1 border-t border-gray-200 xl:flex"}
   [:section {:class-name "min-w-0 flex-1 h-full flex flex-col overflow-hidden xl:order-last"}
    [:div {:class-name "flex-shrink-0 bg-white border-b border-gray-200"}
     (when current-route [(-> current-route :data :view)])]]])

(defn app-container [router]
  (let [current-route @(rf/subscribe [::current-route])]
    [:div.h-full.flex.flex-col
     ; [app-header]
     [:div {:id "sidebar-container"
            :class-name "min-h-0 flex-1 flex overflow-hidden"}
      [app-sidebar router current-route]
      [app-view current-route]]]))

; initialisation events
(rf/reg-fx :push-state
           (fn [route]
             (apply rfe/push-state route)))

(rf/reg-event-fx ::push-state
                 (fn [_ [_ & route]]
                   {:push-state route}))

(rf/reg-event-db ::initialise-db
                 (fn [db _]
                   (if db
                     db
                     {:current-route nil})))

(rf/reg-sub ::current-route
            (fn [db]
              (:current-route db)))

(rf/reg-event-db ::navigated
                 (fn [db [_ new-match]]
                   (assoc db :current-route new-match)))

(defn temp-component []
  [:div
   [:button {:type       :submit
             :class-name "group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"}
    [:span.absolute.left-0.inset-y-0.flex.items-center.pl-3
     [:> icon-solid/LockClosedIcon {:class-name "h-5 w-5 text-indigo-500 group-hover:text-indigo-400"}]]
    "Sign in"]])

(defn temp-component-1 []
  [:div.flex.flex-col.w-full.justify-center [:f> Combobox [{:id 1 :title "item 1"}
                                                           {:id 2 :title "item 2"}]]])

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::initialise-db])
  (init-routes!)
  (dom/render [app-container app-router]
              (.getElementById js/document "root")))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))


(defn init []
  (prn "init")
  (rf/clear-subscription-cache!)
  (start))