(ns escooter
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require [nextjournal.clerk :as clerk])
  (:import [java.time LocalDate]))

(clerk/html
 [:<>
  [:link {:rel "stylesheet"
          :href "https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"
          :crossorigin ""}]
  [:h1.text-xs.text-slate-500 "E-Scooter Beschränkungen Wien"]])

^{::clerk/visibility {:result :hide}}
(def location
  (atom {:lat "48.2052886"
         :lng "16.3423715"}))

^{::clerk/no-cache true
  ::clerk/visibility {:result :hide}}
(def cljs-code (slurp "notebooks/leaflet.cljs"))

^{::clerk/visibility {:result :hide}}
(clerk/eval-cljs-str cljs-code)



^{::clerk/visibility {:result :hide}}
(def leaflet
  {:transform-fn clerk/mark-presented
   :render-fn 'leaflet/escooter-leaflet-render})


^{::clerk/width :full}
(clerk/with-viewer leaflet
  @location)

(clerk/html
 [:div
  [:span.font-mono.text-2 "Legende"]
  [:img {:src "https://www.komendera.com/gis/e-scooter-legend.png"
         :width "200"}]])


^::clerk/no-cache
(clerk/html
 [:div.text-xs.font-mono.text-slate-500
  [:p
   "Karte:"
   [:a {:href "https://basemap.at"} "basemap.at"] " | "
   "Datenquelle: Stadt Wien – "
   [:a {:href "https://data.wien.gv.at"} "data.wien.gv.at"] " | "
   "Datensatz: "
   [:a {:href "https://www.data.gv.at/katalog/dataset/a454ea84-6b4f-4961-a511-bf47ef6c1bd0" }
    "E-Scooter-Beschränkungszonen Wien"]]
  [:p "Erstellt am " (str (LocalDate/now)) " von " [:a.text-blue.underline {:href "https://wien.rocks/@DieterKomendera"} "Dieter Komendera"]]])
