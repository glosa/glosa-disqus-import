(ns glosa-disqus-import.core
  (:require
   [clojure.xml :as xml]
   [xmltojson.xmltojson :as xml-to-json]
   [clojure.walk :as walk]
   [clojure.java.io :as io]
   [clj-time.coerce :as c]
   )
  (:gen-class))

(defn parse-xml-file
  "Parse file XML to hash-map"
  [file]
  (xml-to-json/parse (xml/parse (io/file (io/resource file)))))

(defn get-threads
  "Get all threads"
  [lists]
  (let [all-threads (doall (-> lists :disqus :thread))]
    (filter (fn [thread] (every? true? [(= (-> thread :isClosed) "false") (= (-> thread :isDeleted) "false")])) all-threads)))

(defn get-posts
  "Get all posts"
  [lists]
  ;; {:isDeleted false, :parent {:@dsq:id 4494152992}, :createdAt 2020-02-27T16:56:03Z, :author {:name Cruz Arnulfo Gonzalez Dzib, :username cruzarnulfogonzalezdzib, :isAnonymous false}, :isSpam false, :thread {:@dsq:id 3989649477}, :id nil, :@dsq:id 4812781235, :message <p>Pero es igual de espía que los de EU, no me deja acomodar, agregar o eliminar favoritos, tengo mi cuenta, y a cada rato me la desactiva para pedir datos personales.</p>}
  (let [all-posts         (doall (-> lists :disqus :post))
        filter-posts      (filter (fn [post] (every? true? [(= (-> post :isSpam) "false") (= (-> post :isDeleted) "false")])) all-posts)
        remove-keys-posts (->> filter-posts
                               (map (fn [post] (dissoc post :isSpam))) ;; Remove by key
                               (map (fn [post] (dissoc post :isDeleted)))
                               (map (fn [post] (dissoc post :id)))
                               (map (fn [post] (update-in post [:author] dissoc :isAnonymous))) ;; Remove by Key two level
                               )
        rename-keys-posts (walk/postwalk-replace {(keyword "@dsq:id") :id} remove-keys-posts) ;; Rename all keys @dsp:id to :id
        update-dates      (map (fn [post] (assoc post :createdAt (/ (c/to-long (-> post :createdAt)) 1000))) rename-keys-posts) ;; Datos UTC to Unixtime
        ]
    (doall update-dates)
    )
  )

(defn -main
  "Run"
  [& args]
  (println (get-posts (parse-xml-file "disqus.xml"))))
