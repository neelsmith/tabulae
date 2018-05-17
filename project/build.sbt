

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")


libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.3.0"
)
