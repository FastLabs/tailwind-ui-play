(ns component.combobox
  (:require [reagent.core :as ra]
            [clojure.string :as str]
            ["@headlessui/react" :as ui]
            ["@heroicons/react/solid" :refer [LockClosedIcon SelectorIcon CheckIcon]]))

;TODO: go back and check the fragment with the transition
(defn Combobox [items]
  (let [state (ra/atom {:query    ""
                        :selected (first items)})]
    (fn []
      (let [filtered-items (if (str/blank? (:query @state))
                             items
                             (filter (fn [{:keys [title]}]
                                       (str/includes? (str/lower-case title) (str/lower-case (:query @state)))) items))]
        [:div {:class-name "w-72 fixed top-16"}
         [:> ui/Combobox {:value     (:selected @state)
                          :on-change (fn [new-selected]
                                       (swap! state assoc :selected new-selected))}
          [:div {:class-name "relative mt-1"}
           [:div {:class-name "relative w-full text-left bg-white rounded-lg shadow-md cursor-default focus:outline-none focus-visible:ring-2 focus-visible:ring-opacity-75 focus-visible:ring-white focus-visible:ring-offset-teal-300 focus-visible:ring-offset-2 sm:text-sm overflow-hidden"}
            [:> ui/Combobox.Input {:class-name    "w-full border-none focus:ring-0 py-2 pl-3 pr-10 text-sm leading-5 text-gray-900"
                                   :display-value (fn [item]
                                                    (.-title item))
                                   :on-change     (fn [ev] (swap! state assoc :query (.-value (.-target ev))))}]
            [:> ui/Combobox.Button {:class-name "absolute inset-y-0 right-0 flex items-center pr-2"}
             [:> SelectorIcon {:class-name  "w-5 h-5 text-gray-400"
                               :aria-hidden true}]]]
           [:> ui/Combobox.Options {:class-name "absolute w-full py-1 mt-1 overflow-auto text-base bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"}
            (if (and (= 0 (count filtered-items)) (str/blank? (:query @state)))
              [:div {:class-name "cursor-default select-none relative py-2 px-4 text-gray-700"} "Nothing found"]
              [:<>
               (for [{:keys [title id] :as item} filtered-items]
                 ^{:key id} [:> ui/Combobox.Option {:class-name (fn [x]
                                                                  (str "cursor-default select-none relative py-2 pl-10 pr-4"
                                                                       (if (.-active x) "text-white bg-teal-600" "text-gray-900")))
                                                    :value      item}


                             [:f> (fn [rendered-item]
                                    (let [{:keys [selected]} (js->clj rendered-item)]
                                      [:<>
                                       [:span {:class-name (str "block truncate" (if selected "font-medium" "font-normal"))} title]
                                       (when selected [:span
                                                       [CheckIcon {:class-name  "w-5 h-5"
                                                                   :aria-hidden true}]])]))]])])]]]]))))