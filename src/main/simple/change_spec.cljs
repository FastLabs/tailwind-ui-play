(ns simple.change-spec)


(def system-meta {"Giraffe" {:initials    "G"
                             :bg-color    "bg-pink-600"
                             :environment #{"EMEA" "US" "APAC"}}
                  "Racoon"  {:initials    "R"
                             :bg-color    "bg-purple-600"
                             :environment #{"EMEA" "US" "APAC"}}
                  "Walrus" {:initials "W"
                            :bg-color ""}})

(def change-specs [{:change-id     "giraffe-business-config-change-1"
                    :change-title  "Business Config Change 1"
                    :target-system "Giraffe"
                    :change-type   :business-config
                    :modifications {"walrus" {:resource "/path-to/config.json"}}
                    :service-now   {:template-name ""}}
                   {:change-id     "racoon-sys-config-1"
                    :target-system "Racoon"
                    :change-title  "Racoon Book Family"
                    :change-type   :system-config
                    :modifications {"Walrus" {:resource "/path-to/business-families.json"}}}])

;potentially a search index for different matches
(def change-index {"giraffe-business-config-change-1"
                   #{"match1" "match2"}})
