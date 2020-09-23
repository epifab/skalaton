import sbt._

object BuildDependencies {

  val circeVersion = "0.13.0"
  val catsVersion = "2.0.0"
  val http4sVersion = "0.21.7"

  lazy val Cats = Seq(
    "org.typelevel" %% "cats-core"   % catsVersion,
    "org.typelevel" %% "cats-effect" % catsVersion
  )

  lazy val Circe = Seq(
    "io.circe" %% "circe-core"    % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser"  % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,
    "io.circe" %% "circe-refined" % circeVersion
  )

  lazy val Logging = Seq(
    "ch.qos.logback" % "logback-core" % "1.2.3",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "net.logstash.logback" % "logstash-logback-encoder" % "5.3"
  )

  lazy val Testing = Seq(
    "org.scalatest" %% "scalatest" % "3.1.0" % Test,
    "org.scalamock" %% "scalamock" % "4.4.0" % Test
  )

  lazy val Http4s = Seq(
    "org.http4s" %% "http4s-dsl"   % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion
  )

  lazy val TemplateEngine = Seq(
    "org.scalatra.scalate" %% "scalate-core" % "1.9.5"
  )
}
