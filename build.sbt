lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.10"
lazy val supportedScalaVersions = List(scala212, scala211)

ThisBuild / scalaVersion := scala212
ThisBuild / turbo := true

lazy val root = (project in file("."))
  .aggregate(crossed.js, crossed.jvm)
  .settings(
        crossScalaVersions := Nil,
        publish / skip := true
    )

lazy val crossed = crossProject(JSPlatform, JVMPlatform).in(file(".")).
    settings(

      name := "tabulae",
      organization := "edu.holycross.shot",
      version := "6.5.0",
      scalaVersion := "2.12.4",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),
      resolvers += Resolver.jcenterRepo,
      resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.0.1" % "test",
        "org.wvlet.airframe" %%% "airframe-log" % "20.5.2",

        "edu.holycross.shot.cite" %%% "xcite" % "4.3.0",
        "edu.holycross.shot" %%% "latphone" % "3.0.0"
      )

    ).
    jvmSettings(
      libraryDependencies ++=  Seq(
        "com.github.pathikrit" %% "better-files" % "3.5.0"
      )

    ).
    jsSettings(
      // JS-specific settings:
      scalaJSUseMainModuleInitializer := true,
    )
    lazy val docs = project       // new documentation project
        .in(file("docs-build")) // important: it must not be docs/
        .dependsOn(crossed.jvm)
        .enablePlugins(MdocPlugin)
        .settings(
          mdocIn := file("docs-src"),
          mdocOut := file("testDocs"),
          mdocExtraArguments := Seq("--no-link-hygiene"),
          mdocVariables := Map(
            "VERSION" -> "6.4.0"
          )

        )
