(ns net.readmarks.reloaded.core
  (:require [clojure.tools.logging :as logging]
            [net.readmarks.compost :as compost]))

(defn set-uncaught-exception-handler []
  (Thread/setDefaultUncaughtExceptionHandler
    (reify Thread$UncaughtExceptionHandler
      (uncaughtException [_ thread throwable]
        (logging/error throwable "Uncaught exception in thread" thread)))))

(defn with-shutdown-hook [handler body]
  (let [shutdown-hook (doto (Thread. ^Runnable handler)
                        (.setName "Shutdown hook"))
        rt (Runtime/getRuntime)]
    (.addShutdownHook rt shutdown-hook)
    (try
      (body)
      (finally
        (.removeShutdownHook rt shutdown-hook)))))

; TODO Split user namespace into reset/go and repl, Leiningen template

(defn start [system]
  (set-uncaught-exception-handler)
  (with-shutdown-hook #(compost/stop system)
    (fn []
      (try
        (compost/start system)
        (catch Exception ex
          (logging/error ex "Error starting system")
          (when-let [partially-started (-> ex ex-data :system)]
            (logging/warn "Restoring system")
            (compost/stop partially-started)))))))
