(ns component.dropdown
  (:require ["@headlessui/react" :refer [Transition] :as ui]
            [reitit.frontend.easy :as rfe]
            ["@heroicons/react/solid" :as solid]))



(defn ContextMenu [menu-items]
  [:> ui/Menu {:as         "div"
               :class-name "flex-shrink-0 pr-2"}
   [:div
    [:> ui/Menu.Button {:class-name "w-8 h-8 bg-white inline-flex items-center justify-center text-gray-400 rounded-full hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500"}
     [:> solid/DotsVerticalIcon {:className   "w-5 h-5"
                                 :aria-hidden true}]]]
   [:> ui/Menu.Items {:class-name "z-10 mx-3 origin-top-right absolute right-0 top-10 w-48 mt-1 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 divide-y divide-gray-200 focus:outline-none"}
    [:div {:class-name "px-1 py-1 "}
     (for [menu-item menu-items]
       [:> ui/Menu.Item {:key        (:id menu-item)
                         :as         "div"
                         :class-name (fn [x]
                                       (str (if (.-active x) "bg-gray-100 text-gray-900" "text-gray-700") " block px-4 py-2 text-sm"))}
        [:f> (fn [_]
               [:a {:href (rfe/href (:route-name menu-item))}
                (:item-title menu-item)])]])]]])
