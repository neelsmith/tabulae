

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")


libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "2.7.1"
)
