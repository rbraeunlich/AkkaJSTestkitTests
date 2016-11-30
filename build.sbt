val akkaVersion = "2.4.14"

val scalatestVersion = "3.0.0"

lazy val root = crossProject.in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "AkkaJSTest",
    version := "1.0",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % scalatestVersion % "test"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "eu.unicredit" %%% "akkatestkit" % ("0." + akkaVersion + "-SNAPSHOT"),
      "eu.unicredit" %%% "akkajsactor" % ("0." + akkaVersion)
    )
  )

lazy val rootJS = root.js
lazy val rootJVM = root.jvm


