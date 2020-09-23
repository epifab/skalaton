package skalaton.webapp.routes

import cats.effect.{Blocker, ContextShift, IO}
import org.http4s.Method.GET
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, StaticFile}

object Static {

  def routes(baseDir: String, blocker: Blocker)(implicit cs: ContextShift[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / version / fileName =>
      StaticFile
        .fromResource[IO](s"/$baseDir/$fileName", blocker)
        .getOrElseF(NotFound("Resource not found"))
  }

}
