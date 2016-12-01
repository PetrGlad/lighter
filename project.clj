(defproject net.readmarks/lighter "0.1.0-SNAPSHOT"
  :description "Template for main function and 'reloaded' workflow."
  :url "http://github.com/PetrGlad/lighter"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [net.readmarks/compost "0.1.0-SNAPSHOT"]
                 [org.clojure/tools.logging "0.3.1"]]

  :profiles
  {:dev
   {:source-paths ["dev"]
    :dependencies [[org.clojure/tools.namespace "0.2.11"]]}})
