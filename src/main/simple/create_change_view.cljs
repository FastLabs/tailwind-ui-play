(ns simple.create-change-view
  (:require ["@heroicons/react/solid" :as solid]))

(defn form-title [title]
  [:div {:class "text-sm font-medium text-gray-500"} title])

(defn form-text [title value]
  [:div {:class "sm:col-span-1"}
   [form-title title]
   [:div {:class "mt-1 text-sm text-gray-900"} value]])

(defn form-memo
  ([title memo]
   (form-memo {} title memo))
  ([opts title memo]
   [:div {:class "sm:col-span-2"}
    [form-title title]
    [:div {:class "mt-1 text-sm text-gray-900"}
     [:textarea#comment.shadow-sm.block.w-full.focus:ring-indigo-500.focus:border-indigo-500.sm:text-sm.border.border-gray-300.rounded-md
      (merge {:name          "comment"
              :rows          4
              :cols          110
              :default-value memo
              :placeholder   "Add a note"} opts)]]]))

(defn form-time-window [time-window]
  [:div {:class "sm:col-span-2"}
   [form-title "Change Window"]])


;TODO: replace this with an upload form
(defn form-attachments [attachments]
  [:div {:class "sm:col-span-2"}
   [:div {:class "text-sm font-medium text-gray-500"} "Attachments"]
   [:div {:class "mt-1 text-sm text-gray-900"}
    [:ul {:role "list" :class "border border-gray-200 rounded-md divide-y divide-gray-200"}
     (for [attachment attachments]
       [:li {:key (:name attachment) :class "pl-3 pr-4 py-3 flex items-center justify-between text-sm"}
        [:div {:class "w-0 flex-1 flex items-center"}
         [:> solid/PaperClipIcon {:class "flex-shrink-0 h-5 w-5 text-gray-400" :aria-hidden "true"}]
         [:span {:class "ml-2 flex-1 w-0 truncate"} (:name attachment)]]
        [:div {:class "ml-4 flex-shrink-0"}
         [:a {:href (:href attachment) :class "font-medium text-blue-600 hover:text-blue-500"} "Download"]]])]]])

(defn form-label
  ([label]
   (form-label nil label))
  ([for label]
   [:label {:htmlFor for :className "block text-sm font-medium text-gray-900"} label]))

(defn form-input
  ([label value]
   (form-input {:name "input-1"} label value))
  ([{:keys [name] :as opts} label value]
   [:div {:class "sm:col-span-2"}
    [form-label name label]
    [:div {:className "mt-1"}
     [:input (merge {:type          "text"
                     :default-value value
                     :className     "shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"}
                    opts)]]]))

(defn modifications-view [])

(defn section-title [title description]
  [:div {:class "px-4 py-5 sm:px-6 w-full"}
   [:h2 {:id    "applicant-information-title"
         :class "text-lg leading-6 font-medium text-gray-900"} title]
   [:p {:class "mt-1 max-w-2xl text-sm text-gray-500"} description]])


(defn section [{:keys [title description]} section-content]
  [:div {:class "mt-8 max-w-3xl mx-auto grid grid-cols-1 gap-6 sm:px-6 lg:max-w-7xl lg:grid-flow-col-dense lg:grid-cols-3"}
   [:div {:class "space-y-6 lg:col-start-1 lg:col-span-2"}
    [:section {:aria-labelledby "notes-title"}
     [:div {:class "bg-white shadow sm:rounded-lg"}
      [section-title title description]
      section-content]]]])

(defn new-change-view []
  (let [{:keys [change-title
                bg-color
                initials
                logo-url
                target-system
                service-now]} {:change-title  "Business Config Change 1"
                               :target-system "Giraffe"
                               :initials      "GR"
                               :bg-color      "bg-pink-600"
                               :service-now   {:template-name     "giraffe EMEA business config"
                                               :short-description "Simple giraffe business change"}}]
    [:main {:class " py-10 space-y-6"}
     [:div {:class "max-w-3xl mx-auto px-4 sm:px-6 md:flex md:items-center md:justify-between md:space-x-5 lg:max-w-7xl lg:px-8"}
      [:div {:class "flex items-center space-x-5"}
       [:div {:class "flex-shrink-0"}
        [:div {:class "relative"}
         (if logo-url
           [:img {:class "h-16 w-16 rounded-full"
                  :src   logo-url
                  :alt   ""}]
           [:span {:class (str bg-color " flex-shrink-0 flex items-center justify-center w-16 text-white text-sm font-medium  h-16 rounded-full")
                   :alt   target-system}
            initials])
         [:span {:class       "absolute inset-0 shadow-inner rounded-full"
                 :aria-hidden "true"}]]]
       [:h1 {:class "text-2xl font-bold text-gray-900"} (str target-system " " change-title)]]
      [:div {:className "flex-none items-center justify-between sm:flex-shrink-0 sm:justify-start"}
       [:span {:className "inline-flex items-center px-3 py-0.5 rounded-full text-sm font-medium bg-red-100 text-red-800"} "PROD"]]
      [:div.mt-6.flex.flex-col-reverse.justify-stretch.space-y-4.space-y-reverse.sm:flex-row-reverse.sm:justify-end.sm:space-x-reverse.sm:space-y-0.sm:space-x-3.md:mt-0.md:flex-row.md:space-x-3
       [:button {:class "inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-rose-600 hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-900"
                 :type  "button"} "Create New RFC"]]]

     [section
      {:title       "Change Parameters"
       :description "All required Service Now parameters."}
      [:div {:class "border-t border-gray-200 px-4 py-5 sm:px-6"}
       [:form {:class "grid grid-cols-1 gap-x-4 gap-y-8 sm:grid-cols-2"}
        ; [form-text "Template Name" (:template-name service-now)]
        [form-text "Work Activity" "FO Configuration"]
        ;[form-text "Environment" "PROD"]
        [form-text "Approval Groups" "Approval group"]
        [form-text "Impacted Business Areas" "BUSINESS_AREA_1"]
        [form-text "Implementer Group" "GROUP_1"]
        [form-text "Region" "Europe"]
        [form-text "Country" "UK"]
        [form-input "Short Description" (:short-description service-now)]
        [form-memo {:placeholder "Change Description"} "Description" (:description service-now)]
        [form-time-window {}]
        [form-attachments [{:name "Request email"}]]]]]
     [section {:title       "Modifications"
               :description "System Specific Modifications"}
      [:div {:class "border-t border-gray-200 px-4 py-5 sm:px-6"}
       [:div {:class "grid grid-cols-1 gap-x-4 gap-y-8 sm:grid-cols-2"}
        [form-text "Modification Owner" "Walrus"]
        [form-text "Modification Details" "path-to/desk-config.json"]]]]]))