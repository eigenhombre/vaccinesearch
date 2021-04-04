(ns vaccine.geom
  (:import [java.lang Math]))

(defn distance
  "
  Distance between two lat/lon pairs in miles.  Adapted from
  https://codereview.stackexchange.com/questions/156722/\\
  haversine-formula-in-clojure
  "
  [lat1 lat2 lng1 lng2]
  (let [earth-radius-miles 3963
        lat1 (Math/toRadians lat1)
        lng1 (Math/toRadians lng1)
        lat2 (Math/toRadians lat2)
        lng2 (Math/toRadians lng2)
        half-dlat (/ (- lat2 lat1) 2)
        half-dlng (/ (- lng2 lng1) 2)
        sin2 (fn [x] (Math/pow (Math/sin x) 2))
        a (+ (sin2 half-dlat)
             (* (Math/cos lat1)
                (Math/cos lat2)
                (sin2 half-dlng)))]
    (* 2
       earth-radius-miles
       (Math/atan2 (Math/sqrt a)
                   (Math/sqrt (- 1 a))))))

