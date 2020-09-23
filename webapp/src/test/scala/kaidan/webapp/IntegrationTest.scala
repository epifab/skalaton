package kaidan.webapp

import cats.data.Kleisli
import cats.effect.{Blocker, IO, Resource}
import org.http4s.implicits._
import org.http4s.{Request, Response}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

trait IntegrationTest extends AnyFreeSpec with Matchers {
  val appContext: AppContext = AppContext("TEST", scalaJsFullOpt = false)

  private val app: Resource[IO, Kleisli[IO, Request[IO], Response[IO]]] =
    Blocker[IO].map(blocker => Main.routes(appContext, blocker).orNotFound)

  def run(request: Request[IO]): IO[Response[IO]] =
    app.use(_.run(request))

}
