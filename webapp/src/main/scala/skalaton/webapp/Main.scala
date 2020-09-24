package skalaton.webapp

import cats.effect.{Blocker, ExitCode, IO, IOApp}
import skalaton.webapp.routes.{Health, Home, PeopleApi, Static}
import org.fusesource.scalate.TemplateEngine
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.GZip
import skalaton.domain.repositories.PersonRepo
import skalaton.webapp.repositories.{InMemoryAddressRepo, InMemoryPersonRepo}
import skalaton.webapp.services.PeopleServiceProvider

import scala.concurrent.ExecutionContext.global

case class AppContext(version: String, scalaJsFullOpt: Boolean)

object Main extends IOApp {

  def routes(appContext: AppContext, blocker: Blocker): HttpRoutes[IO] = {
    val templateEngine = new TemplateEngine()
    val peopleService = new PeopleServiceProvider[IO](
      InMemoryPersonRepo,
      InMemoryAddressRepo
    )

    Router(
      "/health" -> Health.routes(appContext.version),
      "/api" -> PeopleApi.routes(peopleService),
      "/assets" -> Static.routes("assets", blocker),
      "/static" -> Static.routes("static", blocker),
      "/" -> Home.routes(templateEngine, appContext.version, appContext.scalaJsFullOpt)
    )
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val context = AppContext(
      sys.env.getOrElse("BUILD_NUMBER", "LOCAL"),
      sys.env.get("SCALAJS_FULL_OPT").map(_.toBoolean).forall(identity)
    )

    val webApp = for {
      blocker <- Blocker[IO]
      server <- BlazeServerBuilder[IO](global)
        .withHttpApp(GZip(routes(context, blocker)).orNotFound)
        .bindHttp(8080, "0.0.0.0")
        .resource
    } yield server

    webApp.use(_ => IO.never).as(ExitCode.Success)
  }

}
