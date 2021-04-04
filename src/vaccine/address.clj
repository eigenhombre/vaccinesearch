(ns vaccine.address
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [vaccine.cache :refer [get-cache!
                                   update-cache!]]
            [vaccine.io :refer [perr]]))

(defn lookup-property! [street
                        city
                        state
                        zip]
  (if-let [r (get-cache! [street city state zip])]
    r
    (do
      (Thread/sleep 1100)
      ;; Rate limit: see TOS at
      ;; https://operations.osmfoundation.org/policies/nominatim/
      (perr "Warning: cache lookup for"
            street city state zip)
      (let [{:keys [status body]}
            (http/get "https://nominatim.openstreetmap.org/search"
                      {:accept :json
                       :query-params {"street" street
                                      "city" city
                                      "state" state
                                      "postcalcode" zip
                                      "limit" 2
                                      "format" "json"}
                       :headers {:User-Agent "Clojure REPL.  Simplicity FTW!"}})
            _ (assert (= 200 status))
            result (vec (json/parse-string body true))]
        (update-cache! [street city state zip] result)
        result))))

(comment
  (lookup-property! "2010 Madison St."
                    "Madison"
                    "WI"
                    "53711")

  ;;=>
  [{:osm_type "way",
    :boundingbox ["43.063093351977" "43.063193351977"
                  "-89.418273338983" "-89.418173338983"],
    :type "house",
    :licence "Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright",
    :place_id 295099853,
    :class "place",
    :lon "-89.41822333898342",
    :lat "43.063143351977175",
    :display_name "2010, Madison Street, Oakland Heights, Vilas, Bowens Addition, Madison, Dane
County, Wisconsin, 53711, United States",
    :osm_id 495765390,
    :importance 0.531}])
