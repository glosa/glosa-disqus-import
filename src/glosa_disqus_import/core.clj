(ns glosa-disqus-import.core
  (:require
   [clojure.xml :as xml]
   [clojure.java.io :as io])
  (:gen-class))


(defn parse-xml-file
  [file]
  (xml/parse (io/file (io/resource file))))

(defn get-threads
  [lists]
  (let [all-threads (get-in lists [:content] [:tag :thread])]

    (filter #(= (get-in % [:tag :isClosed]) false) all-threads)

    )
  )

(defn -main
  "Run"
  [& args]
  (println (get-threads (parse-xml-file "disqus.xml"))))
