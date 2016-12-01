(ns net.readmarks.lighter.composter
  (:require [net.readmarks.lighter.boot :as boot]
            [clojure.tools.logging :as logging]
            [net.readmarks.compost :as compost]))

(defn start [system]
  (try
    (swap! system compost/start)
    (catch Exception ex
      ;; Trying to have both most accurate system state and exceptions logged/rethrown
      (when-let [partially-started (-> ex ex-data :system)] ;; TODO check that exception has compost's exception info type tag
        (logging/error "Startup failed. Trying to recover system.")
        (try
          (reset! system (compost/stop partially-started))
          (catch Exception ex2
            (logging/error ex "Unrecoverable startup error.")
            (throw ex2))))
      (throw ex))))

(defn start-main [system]
  (boot/init-main #(swap! system compost/stop))
  (start system))
