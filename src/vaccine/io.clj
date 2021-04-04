(ns vaccine.io)

(defn perr
  "
  `println` but for `stderr`.
  "
  [& args]
  (binding [*out* *err*]
    (apply println args)))
