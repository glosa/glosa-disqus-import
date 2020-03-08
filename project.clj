(defproject glosa-disqus-import "1.0.1"
  :description "Export XML Disqus to JSON"
  :url "https://programadorwebvalencia.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [xmltojson "0.1.1"]
                 [clj-time "0.15.2"]
                 [cheshire "5.10.0"]]
  :main ^:skip-aot glosa-disqus-import.core
  :target-path "dist/%s"
  :profiles {:uberjar {:aot :all}})
