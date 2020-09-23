package kaidan.webapp.routes

import cats.effect.IO
import cats.implicits._
import kaidan.webapp.IntegrationTest
import org.http4s.Method.GET
import org.http4s.headers.`Content-Type`
import org.http4s.implicits._
import org.http4s.{MediaType, Request, Status}

class HomeSpec extends IntegrationTest {
  "/" - {
    "returns OK" in {
      val response = run(Request[IO](GET, uri"/")).unsafeRunSync()
      response.status mustBe Status.Ok
      response.headers.toList must contain(`Content-Type`(MediaType.text.html))
      response.bodyText.compile.foldMonoid.unsafeRunSync()
    }
  }
}
