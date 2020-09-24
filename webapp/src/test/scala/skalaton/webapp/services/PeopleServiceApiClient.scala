package skalaton.webapp.services

import java.util.UUID

import cats.effect.IO
import io.circe.syntax._
import org.http4s.Method.{DELETE, GET, POST}
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.implicits._
import org.http4s.{Request, Response, Uri}
import skalaton.domain.services.{AddPersonRequest, PeopleService, PersonDetails, ServiceError}

class PeopleServiceApiClient(client: Request[IO] => IO[Response[IO]]) extends PeopleService[IO] {

  private def handleError[T](response: Response[IO]): IO[Left[ServiceError, T]] =
    response.as[ServiceError]
      .attempt
      .map {
        case Left(e) => Left(ServiceError.InternalError(e.getMessage))
        case Right(e) => Left(e)
      }


  override def addPerson(request: AddPersonRequest): IO[Either[ServiceError, PersonDetails]] =
    client(Request(POST, uri"/api/people").withEntity(request.asJson)).flatMap {
      case okResponse if okResponse.status.isSuccess => okResponse.as[PersonDetails].map(Right(_))
      case notOk => handleError(notOk)
    }

  override def removePerson(id: UUID): IO[Either[ServiceError, Unit]] =
    client(Request(DELETE, Uri.unsafeFromString(s"/api/people/$id"))).flatMap {
      case okResponse if okResponse.status.isSuccess => IO.pure(Right(()))
      case notOk => handleError(notOk)
    }

  override def findAllPeople: IO[Either[ServiceError, List[PersonDetails]]] =
    client(Request(GET, uri"/api/people")).flatMap {
      case okResponse if okResponse.status.isSuccess => okResponse.as[List[PersonDetails]].map(Right(_))
      case notOk => handleError(notOk)
    }

}
