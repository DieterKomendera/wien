(ns leaflet
  (:require [nextjournal.clerk.render :as r]
            [nextjournal.clerk.viewer :as v]))



(def mapserver-url "http://qgis.komendera.com/qgisserver?MAP=/home/qgis/projects/Mist-Guide.qgz")

(defn leaflet-radius-label [l latlng]
  (let [icon
        (.divIcon
         l
         (clj->js {:html
                   "
<svg
  viewBox='0 0 50 20'
  version='1.1'
  preserveAspectRatio='none'
  xmlns='http://www.w3.org/2000/svg'>

<rect x='0' y='0' width='90%' height='90%'
fill='white' fill-opacity='80%' rx='4'/>

 <text stroke='#ff0000' stroke-width='1px'
 dy='.5em'
 x='5' y='7'
>200m</text>

</svg>"
                   :className ""
                   :iconSize [50 40]
                   :iconAnchor [25 20]}))]
    (.marker l
             latlng
             (clj->js {:icon icon}))))

(defn mist-guide-leaflet-render [value]
  (v/html
   (when-let [{:keys [lat lng]} value]
     [r/with-dynamic-import {:module "https://cdn.skypack.dev/leaflet@1.9.3"}
      (fn [leaflet]
        [:div {:style {:height 400}
               :ref
               (fn [el]
                 (when el
                   (when-let [m (.-map el)] (.remove m))
                   (let [m (.map leaflet el (clj->js {:zoomControl false
                                                      :zoomDelta 2
                                                      :zoomSnap 0.5
                                                      :attributionControl false}))

                         location-latlng (.latLng leaflet lat lng)
                         _ (.setView m location-latlng 16)

                         location-marker (.marker leaflet location-latlng)
                         base-circle (.circle leaflet location-latlng 200
                                              (clj->js {:color "#FF0000"}))

                         _ (.addTo base-circle m)

                         label (leaflet-radius-label leaflet
                                                     (.latLng leaflet
                                                              (.getNorth (.getBounds base-circle))
                                                              lng))
                         altstoffsammelstellen (.wms (.-tileLayer leaflet)
                                                     mapserver-url
                                                     (clj->js {:layers "Altstoffsammelstelle"
                                                               :format "image/png"
                                                               :detectRetina true
                                                               :dpi (* 90 (.-devicePixelRatio js/window))
                                                               :tiled true
                                                               :transparent true}))
                         basemap-hidpi-layer (.tileLayer leaflet
                                                         "https://{s}.wien.gv.at/basemap/bmaphidpi/normal/google3857/{z}/{y}/{x}.jpeg"
                                                         (clj->js
                                                          {:subdomains    ["maps" "maps1" "maps2" "maps3" "maps4"]
                                                           :maxZoom       25
                                                           :maxNativeZoom 19
                                                           :attribution   "basemap.at"
                                                           :errorTileUrl  "/transparent.gif"}))]

                     (set! (.-map el) m)

                     (.addTo basemap-hidpi-layer m)

                     (.addTo altstoffsammelstellen m)
                     (.addTo location-marker m)

                     (.addTo label m)

                     )))}])])))

(def escooter-mapserver-url "http://qgis.komendera.com/qgisserver?MAP=/home/qgis/projects/E-Scooter.qgz")
(defn escooter-leaflet-render [value]
  (v/html
   (when-let [{:keys [lat lng]} value]
     [r/with-dynamic-import {:module "https://cdn.skypack.dev/leaflet@1.9.3"}
      (fn [leaflet]
        [:div {:style {:height 600}
               :ref
               (fn [el]
                 (when el
                   (when-let [m (.-map el)] (.remove m))
                   (let [m (.map leaflet el (clj->js {:zoomControl true
                                                      :zoomDelta 2
                                                      :zoomSnap 0.5
                                                      :attributionControl false}))

                         location-latlng (.latLng leaflet lat lng)
                         _ (.setView m location-latlng 13.0)

                         escooter (.wms (.-tileLayer leaflet)
                                        escooter-mapserver-url
                                        (clj->js {:layers "E-Scooter Zonen"
                                                  :format "image/png"
                                                  :detectRetina true
                                                  :dpi (* 90 (.-devicePixelRatio js/window))
                                                  :tiled true
                                                  :transparent true}))
                         basemap-hidpi-layer (.tileLayer leaflet
                                                         "https://{s}.wien.gv.at/basemap/bmaphidpi/normal/google3857/{z}/{y}/{x}.jpeg"
                                                         (clj->js
                                                          {:subdomains    ["maps" "maps1" "maps2" "maps3" "maps4"]
                                                           :maxZoom       25
                                                           :maxNativeZoom 19
                                                           :attribution   "basemap.at"
                                                           :errorTileUrl  "/transparent.gif"}))]

                     (set! (.-map el) m)

                     (.addTo basemap-hidpi-layer m)
                     (.addTo escooter m)
                     )))}])])))
