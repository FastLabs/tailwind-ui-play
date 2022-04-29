(ns simple.main
  (:require [reagent.dom :as dom]
            [re-frame.core :as rf]
            [reitit.core :as route]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend :refer [router]]
            [component.combobox :refer [Combobox]]
            [component.dropdown :refer [ContextMenu]]
            [simple.change-spec :as data]
            [simple.create-change-view :refer [new-change-view]]
            ["@heroicons/react/outline" :refer [InboxIcon] :as outline]
            ["@heroicons/react/solid" :refer [LockClosedIcon] :as solid]
            [clojure.string :as str]))

(defn new-rfc-action [{:keys [environment]}]
  (map (fn [env] {:id         (str "new-rfc-" (str/lower-case env))
                  :item-title (str "New RFC for " env)
                  :route-name ::create-rfc}) environment))

(defn change-spec-list []
  (let [change-specs @(rf/subscribe [::all-change-specs])]
    (prn change-specs)
    [:ul {:class-name "mt-3 grid grid-cols-1 gap-5 sm:gap-6 sm:grid-cols-2 lg:grid-cols-4"
          :role       "list"}
     (for [{:keys [change-id bg-color initials change-title environment] :as change-spec} change-specs]
       [:li {:key        change-id
             :class-name "col-span-1 flex shadow-sm rounded-md relative"}
        [:div {:class-name (str bg-color " flex-shrink-0 flex items-center justify-center w-16 text-white text-sm font-medium rounded-l-md")}
         initials]
        [:div {:className "flex-1 flex items-center justify-between border-t border-r border-b border-gray-200 bg-white rounded-r-md truncate"}
         [:div {:className "flex-1 px-4 py-2 text-sm truncate"}
          [:a {:href      (str "#new-rfc/" change-id)
               :className "text-gray-900 font-medium hover:text-gray-600"}
           change-title]
          [:p {:className "text-gray-500"} (str "Regions: " (str/join ", " environment))]]
         [ContextMenu (new-rfc-action change-spec)]]])]))

(defn template-groups [])

(defn home-page []
  [:div {:class-name "flex flex-col w-0 flex-1"}
   [:div {:class-name "sticky top-0 z-10 flex-shrink-0 flex h-16 bg-white border-b border-gray-200"}
    [:div {:class-name "flex-1 px-4 flex justify-between"}
     [:div {:class-name "flex-1 flex"}
      [:form {:class-name "w-full flex lg:ml-0" :action "#" :method "GET"}
       [:label {:htmlFor "search-field" :class-name "sr-only"} "Search"]
       [:div {:class-name "relative w-full text-gray-400 focus-within:text-gray-600"}
        [:div {:class-name "absolute inset-y-0 left-0 flex items-center pointer-events-none"}
         [:> solid/SearchIcon {:class-name  "h-5 w-5"
                               :aria-hidden "true"}]]
        [:input#search-field {:class-name    "block w-full h-full pl-8 pr-3 py-2 border-transparent text-gray-900 placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-0 focus:border-transparent sm:text-sm"
                              :placeholder   "Search"
                              :auto-complete "off"
                              :type          "search"
                              :name          "search"}]]]]
     [:div {:class-name "ml-4 flex items-center lg:ml-6"}
      [:button {:type       "button"
                :class-name "inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-rose-600 hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-900"}
       "Create"]]]]
   [:main {:class-name "flex-1"}
    [change-spec-list]]])



(defn list-rfc []
  [:div "List rfc"])

(def routes
  ["/"
   ["" {:name         ::home
        :nav-icon     outline/SparklesIcon
        :start-route? true
        :nav-text     "Home"
        :view         home-page}]
   ["create-rfc" {:name     ::create-rfc
                  :nav-text "Create Rfc"
                  :nav-icon outline/CollectionIcon
                  :view     new-change-view}]
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
          :let [route (route/match-by-name router route-name)
                route-data (:data route)]
          :when (:start-route? route-data)]
      [:a {:href       (rfe/href (-> route-data :name))
           :key        (-> route-data :name (name))
           :class-name (str (if (= (-> current-route :data :name)
                                   route-name) "bg-gray-900 text-white" "text-gray-400 hover:bg-gray-700") " "
                            "flex-shrink-0 inline-flex items-center justify-center h-14 w-14 rounded-lg")}
       [:span {:class-name "sr-only"} (-> route-data :nav-text)]
       [:> (-> route-data :nav-icon) {:class-name  "h-6 w-6"
                                      :aria-hidden true}]])]])

(defn app-view [current-route]
  ;[:main {:class-name "min-w-0 flex-1 border-t border-gray-200 xl:flex"}
  ; [:section {:class-name "min-w-0 flex-1 h-full flex flex-col overflow-hidden xl:order-last"}
  ;   [:div {:class-name "flex-shrink-0 bg-white border-b border-gray-200"}
  (when current-route [(-> current-route :data :view)]))

(defn app-container [router]
  (let [current-route @(rf/subscribe [::current-route])]
    [:div.h-full.flex.flex-col
     ; [app-header]
     [:div {:id         "sidebar-container"
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
                   (merge db
                          {:current-route nil
                           :system-meta   data/system-meta
                           :change-specs  data/change-specs})))


(rf/reg-sub ::current-route
            (fn [db]
              (:current-route db)))

(rf/reg-sub ::all-change-specs
            (fn [{:keys [system-meta change-specs] :as db}]
              (prn "db " db)
              (prn " -" change-specs)
              (map (fn [change-spec]
                     (merge change-spec (get system-meta (:target-system change-spec)))) change-specs)))

(rf/reg-event-db ::navigated
                 (fn [db [_ new-match]]
                   (assoc db :current-route new-match)))

(defn temp-component []
  [:div
   [:button {:type       :submit
             :class-name "group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"}
    [:span.absolute.left-0.inset-y-0.flex.items-center.pl-3
     [:> LockClosedIcon {:class-name "h-5 w-5 text-indigo-500 group-hover:text-indigo-400"}]]
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