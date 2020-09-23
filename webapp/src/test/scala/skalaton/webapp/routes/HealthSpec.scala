package skalaton.webapp.routes

import cats.effect.IO
import skalaton.webapp.IntegrationTest
import org.http4s.Method.GET
import org.http4s.{Request, Status}
import org.http4s.implicits._
import cats.implicits._

class HealthSpec extends IntegrationTest {
  "/health" - {
    "returns OK and the app version" in {
      val response = run(Request[IO](GET, uri"/health")).unsafeRunSync()
      response.status mustBe Status.Ok
      response.bodyText.compile.foldMonoid.unsafeRunSync() mustBe appContext.version
    }
  }
}
