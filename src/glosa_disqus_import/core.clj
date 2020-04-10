(ns glosa-disqus-import.core
  (:require
   [clojure.xml :as xml]
   [xmltojson.xmltojson :as xml-to-json]
   [clojure.walk :as walk]
   [clojure.java.io :as io]
   [clj-time.coerce :as c]
   [cheshire.core :refer [generate-stream]])
  (:gen-class))

(defn parse-xml-file
  "Parse file XML to hash-map"
  [file]
  (xml-to-json/parse (xml/parse (io/file file))))

(defn get-threads
  "Get all threads"
  [lists]
  (let [all-threads            (doall (-> lists :disqus :thread))
        filter-threads-actives (filter (fn [thread] (every? true? [(= (-> thread :isClosed) "false") (= (-> thread :isDeleted) "false")])) all-threads)
        remove-keys-threads    (map (fn [thread] (dissoc thread :id)) filter-threads-actives)
        rename-keys-threads    (walk/postwalk-replace {(keyword "@dsq:id") :id} remove-keys-threads) ;; Rename all keys @dsp:id to :id
        ]
    (do rename-keys-threads)
    ))

(defn get-single-thread
  "Get single threads"
  [lists id]
  (first (filter (fn [thread] (= id (-> thread :id))) (get-threads lists))))

(defn get-posts
  "Get all posts"
  [lists]
  (let [all-posts         (doall (-> lists :disqus :post))
        filter-posts      (filter (fn [post] (every? true? [(= (-> post :isSpam) "false") (= (-> post :isDeleted) "false")])) all-posts)
        remove-keys-posts (->> filter-posts
                               (map (fn [post] (dissoc post :isSpam))) ;; Remove by key
                               (map (fn [post] (dissoc post :isDeleted)))
                               (map (fn [post] (dissoc post :id)))
                               (map (fn [post] (update-in post [:author] dissoc :isAnonymous))) ;; Remove by Key two level
                               )
        rename-keys-posts (walk/postwalk-replace {(keyword "@dsq:id") :id} remove-keys-posts)                                   ;; Rename all keys @dsp:id to :id
        update-dates      (map (fn [post] (assoc post :createdAt (/ (c/to-long (-> post :createdAt)) 1000))) rename-keys-posts) ;; Datos UTC to Unixtime
        update-author     (map (fn [post] (assoc post :author (-> post :author :name))) update-dates)                           ;; Update author
        add-thread        (map (fn [post] (assoc post :thread (get-single-thread lists (-> post :thread :id)))) update-author)  ;; Add thread
        update-thread-id  (map (fn [post] (assoc post :thread (-> post :thread :link))) add-thread)                             ;; Update id thread to :url
        parse-id          (map (fn [post] (assoc post :id (read-string (post :id)))) update-thread-id)
        parse-parent      (map (fn [post] (assoc post :parent (if (contains? post :parent) (read-string (-> post :parent :id)) ""))) parse-id) ;; Add parent
        ]
    (doall parse-parent)))

(defn -main
"Run"
[file]
(let [lists (parse-xml-file file)
      posts (get-posts lists)]
  (generate-stream posts (clojure.java.io/writer (str file ".json")))))
