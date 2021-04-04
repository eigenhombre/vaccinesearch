(ns vaccine.cache
  "
  Trivial durable cache. Don't ever use this for:
  - anything important
  - anything where performance matters
  - multiple threads.
  FIXME: use something better.
  ")

(def cache-path (str (System/getenv "HOME")
                     "/.address-cache"))

(defn read-cache []
  (try
    (clojure.edn/read-string (slurp cache-path))
    (catch java.io.FileNotFoundException _
      {})))

(defn write-cache! [m]
  (spit cache-path (pr-str m)))

(defn update-cache! [k v]
  (let [cache (read-cache)]
    (write-cache! (assoc cache k v))))

(defn get-cache! [k]
  (let [cache (read-cache)]
    (get (read-cache) k)))
