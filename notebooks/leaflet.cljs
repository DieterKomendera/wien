(ns leaflet
  (:require [nextjournal.clerk.render :as r]
            [nextjournal.clerk.viewer :as v]))


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
