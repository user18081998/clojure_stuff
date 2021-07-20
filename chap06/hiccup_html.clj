(ns custom-hiccup 
  (:require [clojure.string :as string]))

(defn attributes [m]
  (string/join " " (map (fn [[k v]]
                          (if (string? v)
                            (str (name k) "=\"" v "\"")
                            (name k)))
                        m)))

(defn keyword->opening-tag [kw] (str "<" (name kw) ">"))
(defn keyword-attributes->opening-tag [kw attrs] (str "<" (name kw) " " (attributes attrs) ">"))
(defn keyword->closing-tag [kw] (str "</" (name kw) ">"))

(defn has-attributes? [tree] (map? (second tree)))
(defn singleton? [tree] (and (vector? tree) (#{:img :meta :link :input :br} (first tree))))
(defn singleton-with-attrs? [tree] (and (singleton? tree) (has-attributes? tree)))
(defn element-with-attrs? [tree] (and (vector? tree) (has-attributes? tree)))

(defn hiccup [tree]
  (cond (not tree) tree
        (string? tree) tree
        (singleton-with-attrs? tree) (keyword-attributes->opening-tag (first tree) (second tree))
        (singleton? tree) (keyword->opening-tag (first tree))
        (element-with-attrs? tree)
        (apply str
               (concat
                [(keyword-attributes->opening-tag (first tree) (second tree))]
                (map hiccup (nnext tree))
                [keyword->closing-tag (first tree)]))
        (vector? tree)
        (apply str
               (concat
                [(keyword->opening-tag (first tree))]
                (map hiccup (next tree))
                [(keyword->closing-tag (first tree))]))))