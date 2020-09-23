package kaidan.webapp.routes

import cats.effect.IO
import org.fusesource.scalate.TemplateEngine
import org.http4s.Method.GET
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.{HttpRoutes, MediaType}

object Home {

  def routes(templateEngine: TemplateEngine, appVersion: String, scalaJsFullOpt: Boolean): HttpRoutes[IO] = {
    val basicModel = Map(
      "appVersion" -> appVersion,
      "depsJsFile" -> (if (scalaJsFullOpt) "frontend-jsdeps.min.js" else "frontend-jsdeps.js"),
      "optJsFile" -> (if (scalaJsFullOpt) "frontend-opt.js" else "frontend-fastopt.js"),
    )

    HttpRoutes.of[IO] {
      case GET -> Root => Ok(templateEngine.layout("templates/home.mustache", basicModel), `Content-Type`(MediaType.text.html))
    }
  }

}
