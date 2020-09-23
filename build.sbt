import BuildDependencies._
import sbt.Global
import sbt.Keys.{resolvers, scalacOptions}
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

/*****************************************************************
 Global Settings & Configs
 *****************************************************************/
Global / scalaVersion := "2.13.1"
Global / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings",
  "-language:existentials",
  "-language:higherKinds",
  // "-Xlint:_,-missing-interpolator",
  // Next is to avoid stack overflow exception:
  // https://circe.github.io/circe/codecs/known-issues.html
  "-J-Xss64m"
)
Global / resolvers += "jitpack" at "https://jitpack.io"
Global / resolvers += Resolver.sonatypeRepo("releases")
Global / version := sys.env.getOrElse("BUILD_NUMBER", "LOCAL")
Global / organization := "epifab.io"
Global / exportJars := true
Global / parallelExecution := false
Global / scalaJSStage := (if (sys.env.get("FULL_OPT_JS").forall(_.toBoolean)) FullOptStage else FastOptStage)

lazy val domain = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("domain"))
  .settings(libraryDependencies ++= Testing ++ Circe)

lazy val webapp = (project in file("webapp"))
  .dependsOn(domain.jvm % "compile->compile;test->test")
  .enablePlugins(SbtWeb, JavaAppPackaging)
  .settings(
    scalaJSProjects := Seq(frontend),
    exportJars := true,

    resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo),
    resolvers += Resolver.bintrayRepo("hseeberger", "maven"),

    libraryDependencies ++= Logging ++ Testing ++ Cats ++ Circe ++ Http4s ++ TemplateEngine,

    pipelineStages in Assets := Seq(scalaJSPipeline),
    WebKeys.packagePrefix in Assets := "assets/",
    managedClasspath in Runtime += (packageBin in Assets).value
  )

lazy val reactVersion = "16.13.1"
lazy val scalaJsVersion = "1.1.0"

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(domain.js % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0",
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.0.0",
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "org.scala-js" %%% "scalajs-dom" % "1.1.0",
      "com.github.japgolly.scalajs-react" %%% "core" % "1.7.5",
      "com.github.japgolly.scalajs-react" %%% "extra" % "1.7.5"
    ),
    dependencyOverrides += "org.webjars.npm" % "js-tokens" % "3.0.2",
    jsDependencies ++= Seq(
      "org.webjars.npm" % "react" % reactVersion

        /        "umd/react.development.js"
        minified "umd/react.production.min.js"
        commonJSName "React",

      "org.webjars.npm" % "react-dom" % reactVersion

        /            "umd/react-dom.development.js"
        minified     "umd/react-dom.production.min.js"
        dependsOn    "umd/react.development.js"
        commonJSName "ReactDOM",

      "org.webjars.npm" % "react-dom" % reactVersion
        /            "umd/react-dom-server.browser.development.js"
        minified     "umd/react-dom-server.browser.production.min.js"
        dependsOn    "umd/react-dom.development.js"
        commonJSName "ReactDOMServer",

      "org.webjars.npm" % "js-cookie" % "2.2.1"
        /            "js.cookie.js"
        commonJSName "Cookie"
    ),
    scalaJSUseMainModuleInitializer := true,
    skip in packageJSDependencies := false
  )
