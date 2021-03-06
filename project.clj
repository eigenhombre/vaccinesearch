(defproject vaccine "0.1.0-SNAPSHOT"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cheshire "5.10.0"]
                 [clj-http "3.12.1"]]
  :main ^:skip-aot vaccine.core
  :target-path "target/%s"
  :uberjar-name "vaccine.jar"
  :profiles {:uberjar {:aot :all}})
