(ns net.readmarks.lighter.boot
  (:require [clojure.tools.logging :as logging]))

(defn set-uncaught-exception-handler []
  (Thread/setDefaultUncaughtExceptionHandler
    (reify Thread$UncaughtExceptionHandler
      (uncaughtException [_ thread throwable]
        (logging/error throwable "Uncaught exception in thread" thread)))))

(defn make-shutdown-hook [handler]
  (doto (Thread. ^Runnable handler)
    (.setName "shutdown-hook")))

(defn add-shutdown-hook [handler]
  (.addShutdownHook (Runtime/getRuntime)
    (make-shutdown-hook handler)))

(defn remove-shutdown-hook [hook]
  (.removeShutdownHook (Runtime/getRuntime) hook))

(defn init-main [shutdown-fn]
  (set-uncaught-exception-handler)
  (-> (make-shutdown-hook shutdown-fn)
    (add-shutdown-hook)))
