(ns net.readmarks.lighter.reload)

(defn in-app-ns
  "Dynamically resolve ns in server namespace.
   Lazy loads to still have 'user namespace in case of application compilation errors."
  [application-ns-sym sym]
  (require application-ns-sym)
  (if-let [app-ns (find-ns application-ns-sym)]
    (ns-resolve app-ns sym)
    (throw (IllegalStateException. (str "Cannot find namespace " application-ns-sym)))))

(defn update-app [app-ns-sym app update-fn-sym & args]
  (swap! app
    (apply (in-app-ns app-ns-sym update-fn-sym) args)))
