(ns simple.main
      (:require [reagent.dom :as dom]))

(defn ^:dev/after-load start []
      (dom/render [:div "Hello World"]
                  (.getElementById js/document "root")))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))


(defn init []
  (prn "init")
  (start))