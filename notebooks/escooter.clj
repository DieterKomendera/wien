(ns escooter
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require [nextjournal.clerk :as clerk])
  (:import [java.time LocalDate]))

(clerk/html
 [:<>
  [:link {:rel "stylesheet"
          :href "https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"
          :crossorigin ""}]
  [:h1.text-slate-500 "E-Scooter-Beschränkungszonen Wien"]
  [:div.text-xs.text-slate-700
   [:p "Darstellung der Wiener E-Scooter-Beschränkungszonen"]
   [:blockquote.font-mono "Um ein besseres Miteinander im öffentlichen Raum zu gewährleisten, wurden für Sharing-Mobility-Anbieter (z.B. E-Scooter-Anbieter) einige Regeln aufgestellt. In diesem Datensatz werden Zonen definiert, in denen bestimmte Beschränkungen gelten. Es handelt sich dabei um (zeitabhängige) Fahr- und/oder Abstellverbote, Geschwindigkeitsbeschränkungen bzw. Abstellmöglichkeiten. Die Zonen basieren u.a. auf Fußgängerzonen, Begegnungszonen und den Flächen der ständigen Märkte laut Marktordnung."]
   [:p "Quelle: " [:a {:href "https://www.data.gv.at/katalog/dataset/a454ea84-6b4f-4961-a511-bf47ef6c1bd0#resources"} "https://www.data.gv.at"]]]])

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
