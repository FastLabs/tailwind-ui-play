(ns component.listbox
  (:require ["@headlessui/react" :as ui]
            ["@heroicons/react/solid" :as solid]))

(defn listbox
  ([items]
   (listbox {:selected (first items)} items))
  ([{:keys [selected on-change]} items]
   [:div {:class " w-72"}
    [:> ui/Listbox {:value     selected
                    :on-change #(when on-change (on-change %))}
     [:div {:class "relative mt-1"}
      [:> ui/Listbox.Button {:class "relative w-full  border border-gray-300  cursor-default rounded-lg bg-white py-2 pl-3 pr-10 text-left  focus:outline-none focus-visible:border-indigo-500 focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75 focus-visible:ring-offset-2 focus-visible:ring-offset-orange-300 sm:text-sm"}
       [:span {:class "block truncate"}
        (:label selected)]
       [:span {:class "pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2"}
        [:> solid/SelectorIcon {:class       "h-5 w-5 text-gray-400"
                                :aria-hidden true}]]]
      [:> ui/Listbox.Options {:class "z-10 absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"}
       (for [{:keys [id label] :as item} items]
         [:> ui/Listbox.Option {:key   id
                                :class-name (fn [current]
                                              (str "relative cursor-default select-none py-2 pl-10 pr-4 "
                                                   (if (.-active current) "bg-indigo-600 text-white " "text-gray-900")))


                                :value item}
          [:f> (fn [ rendered-item] ;TODO investigate why this is no populated
                 (let [{:keys [selected] :as x} (js->clj rendered-item)]
                   (prn x)
                   [:<>
                    [:span {:class (str "block truncate " (if selected "font-medium" "font-normal"))}
                     label]
                    (when selected
                      [:span {:class "absolute inset-y-0 left-0 flex items-center pl-3 text-amber-600"}
                       [:> solid/CheckIcon {:class       "h-5 w-5"
                                            :aria-hidden true}]])]))]])]]]]))


