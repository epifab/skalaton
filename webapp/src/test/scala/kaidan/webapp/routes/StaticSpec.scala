package kaidan.webapp.routes

import cats.implicits._
import kaidan.webapp.IntegrationTest
import org.http4s.Method.GET
import org.http4s.implicits._
import org.http4s.{Request, Status}

class StaticSpec extends IntegrationTest {
  "/static/<version>/hello-world" - {
    "returns the file content" ignore {
      // todo: it doesn't terminate
      val response = run(Request(GET, uri"/static/anything/hello-world")).unsafeRunSync()
      response.status mustBe Status.Ok
      response.bodyText.compile.foldMonoid.unsafeRunSync() mustBe "Hello world"
    }
  }
}
