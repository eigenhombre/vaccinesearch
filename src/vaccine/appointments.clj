(ns vaccine.appointments
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [vaccine.address :as addr]
            [vaccine.geom :refer [distance]]))

(defn add-distance
  "
  If a `lat` and `lon` are found on the first `geo` item on `m`, use
  the latitude and longitude from user's location `my-loc` to
  determine great circle distance in miles.
  "
  [my-lat my-lon m]
  (let [[{:keys [lat lon]}] (:geo m)
        lat (and lat (Double/parseDouble lat))
        lon (and lon (Double/parseDouble lon))]
    (if (and lat lon)
      (assoc m :distance (distance lat my-lat
                                   lon my-lon))
      m)))

(defn add-lat-lon
  "
  Add latitude and longitude using Nominatum for a particular address.
  "
  [{:keys [postal_code
           city
           state
           address] :as appt}]
  (assoc appt :geo (addr/lookup-property! address
                                          city
                                          state
                                          postal_code)))

(defn appointments
  "
  Use vaccinespotter.org to find appointments for your state and lat/long.
  "
  [state my-lat my-lon]
  (->> state
       (format "https://www.vaccinespotter.org/api/v0/states/%s.json")
       http/get
       :body
       (#(json/parse-string % true))
       :features
       (map :properties)
       (filter :appointments_available)
       (map add-lat-lon)
       (map (partial add-distance my-lat my-lon))))

(comment
  (-> (appointments "IL" {:lat 41.7989, :lon -87.5854})
      first
      :geo
      first
      :lat)
  ;;=>
  "40.56599982749657"
  )
