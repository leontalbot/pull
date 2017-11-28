(ns leontalbot.pull.core
  (:refer-clojure :exclude [get]))

(defn get 
  [coll ks]
  (cond 
    (empty? coll) coll
    
    (empty? ks) coll
    
    (map? coll)
    (let [sub-coll (clojure.core/get coll (first ks))]
      (get sub-coll (rest ks)))
    
    (and (sequential? coll) (some map? coll)) 
    (let [sub-coll (map #(clojure.core/get % (first ks)) coll)]
      (get sub-coll (rest ks)))
    
    :else coll))

(defn pull 
  ([coll query]
   (cond 
     (empty? query) coll
    
     (sequential? query) 
     (reduce 
      (fn [acc i] 
        (cond 
          (keyword? i)
          (let [sub-coll (clojure.core/get coll i)] 
            (assoc acc i sub-coll))
          
          (map? i)
          (reduce 
           (fn [m [k v]] 
             (let [sub-coll (clojure.core/get coll k)]
               (assoc 
                acc
                k 
                (cond (sequential? sub-coll) 
                      (map #(pull % v) sub-coll)
                      
                      (map? sub-coll) 
                      (pull sub-coll v)
                      
                      :else v))))
           acc
           i)
          :else i))
      {}
      query)
     
     :else coll))
  
  ([coll get-query pull-query]
   (-> coll (get get-query) (pull pull-query))))


