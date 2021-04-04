(ns vaccine.core
  (:gen-class)
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [vaccine.appointments :as appt]))

(defn -main [state lat lon & [max-str]]
  (let [maxn (if max-str
               (Integer/parseInt max-str)
               3)]
    (doseq [v (->> (appt/appointments state
                                      (Double/parseDouble lat)
                                      (Double/parseDouble lon))
                   (take maxn)
                   (filter :distance)
                   (sort-by :distance)
                   reverse
                   (map (juxt :provider
                              :distance
                              :name
                              :address
                              :city
                              :state
                              :postal_code
                              (comp (juxt :lat :lon)
                                    first
                                    :geo))))]
      (println v))))

(comment
  (-main "IL" "41.7989" "-87.5854")  ;; 3 sites only
  (-main "IL" "41.7989" "-87.5854" "99999" ))
