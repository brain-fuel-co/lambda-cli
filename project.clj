(defproject lambda-cli "0.1.0-SNAPSHOT"
  :description "CLI App for Lambda Calculus"
  :url "http://brain-fuel.co/cli"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [instaparse "1.4.12"]]
  :main ^:skip-aot lambda-cli.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
