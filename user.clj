(require
 '[nextjournal.clerk :as clerk])

(comment
  (clerk/serve! {:browse? true
                 :port 7775})

  (clerk/show! "notebooks/escooter.clj")

  (clerk/build! {:paths ["notebooks/dtv.clj"]
                 :bundle? true}))
