(ns mongodb.datbase
  (:require [monger.core :as mg]
            [monger.credentials :as mg-creds]
            [monger.collection :as mg-coll]
            [monger.conversion :as mg-conv])
  (:import org.bson.types.ObjectId))

(def conn-uri "mongodb://backend:backend_pass@localhost:3000/somedb?authSource=admin" #_(format "mongodb://%s:%s@%s:%s/somedb" "backend" "backend_pass" "localhost" 3000))

(def conn (mg/connect-via-uri conn-uri))

(defn normalize-query-id [query-map]
  (when (:_id query-map)
    (update query-map :_id #(ObjectId. %))))

(defn normalize-query-result [resutl-map]
  (update resutl-map :_id #(.toString %)))

(defn find-docs [conn coll query-map]
  (let [mongo-query (-> query-map
                        normalize-query-id)]
    (->> (mg-coll/find-maps (:db conn) coll mongo-query)
         (map normalize-query-result)
         vec)))

(let [coll "pets"]

  #_(mg-coll/insert db coll {:name "another cat" :age 13})

  (find-docs conn coll {}))
