
// In sbt 13.x, 2.10 is built-in version, so make sure for
// tasks we use a 2.10 version of included libraries.
scalaVersion := "2.10.6"

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")


libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "2.7.1"
)
