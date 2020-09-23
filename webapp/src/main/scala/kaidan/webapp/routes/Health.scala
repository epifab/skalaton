package kaidan.webapp.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.Method.GET
import org.http4s.dsl.io._

object Health {

  def routes(appVersion: String): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(appVersion)
  }

}
