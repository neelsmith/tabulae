

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")


libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.3.0",

  "com.github.pathikrit" %% "better-files" % "3.5.0"

)
