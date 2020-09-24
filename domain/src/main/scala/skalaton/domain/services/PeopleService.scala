package skalaton.domain.services

import java.time.LocalDate
import java.util.UUID

import io.circe.Codec
import io.circe.generic.semiauto._
import skalaton.domain.model.{Address, Person}

case class PersonDetails(person: Person, address: Option[Address])

object PersonDetails {
  implicit val codec: Codec[PersonDetails] = deriveCodec
}

case class AddPersonRequest(name: String, dateOfBirth: LocalDate, postcode: Option[String])

object AddPersonRequest {
  implicit val codec: Codec[AddPersonRequest] = deriveCodec
}

trait PeopleService[F[_]] {

  def addPerson(request: AddPersonRequest): F[Either[ServiceError, PersonDetails]]

  def removePerson(id: UUID): F[Either[ServiceError, Unit]]

  def findAllPeople: F[Either[ServiceError, List[PersonDetails]]]

}
