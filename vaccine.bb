#!/usr/local/bin/bb

(ns vaccine
  (:require [babashka.curl :as http]
            [cheshire.core :as json]))

(def pi Math/PI)
;; Distance formula:
(defn dist-miles
  "
  https://support.sisense.com/hc/en-us/articles/\\
  230644288-Calculate-Distance-Between-Two-Points-Using-Latitude-and-Longitude
  "
  [lat1 lat2 lon1 lon2]
  (* 3963
     (Math/acos
      ))
  (+ (* Math/sin (/ (* pi lat1) 180.0)
        Math/sin (/ (* pi lat2) 180.0))
     (* Math/cos (/ (* pi lat1) 180.0)
        Math/cos (/ (* pi lat2) 180.0)
        Math/cos (- (* pi lon1) 180.0
                    (* pi lon2) 180.0))))

(def hp-latlong [41.7948 87.5917])

(clojure.pprint/pprint
 (->> "https://www.vaccinespotter.org/api/v0/states/IL.json"
      http/get
      :body
      (#(json/parse-string % true))
      :features
      (filter (comp :appointments_available :properties))
      (sort-by (comp :city :properties))
      (map (comp (juxt :name :city :state identity) :properties))
      (take 3)
      #_(map (partial clojure.string/join "--"))))
(println "ok")
