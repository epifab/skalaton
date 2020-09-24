package skalaton.webapp.routes

import cats.effect.IO
import io.circe.syntax._
import org.http4s.Method.GET
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Response}
import skalaton.domain.services.{AddPersonRequest, PeopleService, ServiceError}

object PeopleApi {
  private def handleError(serviceError: ServiceError): IO[Response[IO]] = serviceError match {
    case ServiceError.NotFound => NotFound()
    case ServiceError.BadRequest(reason) => BadRequest(reason)
    case error => InternalServerError(error.asJson)
  }

  def routes(peopleService: PeopleService[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "people" =>
      peopleService.findAllPeople.flatMap {
        case Right(people) => Ok(people.asJson)
        case Left(error) => handleError(error)
      }

    case r@ POST -> Root / "people" =>
      for {
        request <- r.as[AddPersonRequest]
        result <- peopleService.addPerson(request)
        response <- result match {
          case Right(people) => Ok(people)
          case Left(error) => handleError(error)
        }
      } yield response

    case DELETE -> Root / "people" / UUIDVar(id) =>
      peopleService.removePerson(id).flatMap {
        case Right(_) => NoContent()
        case Left(error) => handleError(error)
      }

  }
}
