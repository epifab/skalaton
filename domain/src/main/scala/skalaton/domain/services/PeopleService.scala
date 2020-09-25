package skalaton.domain.services

import java.time.LocalDate
import java.util.UUID

import io.circe.Codec
import io.circe.generic.semiauto._
import skalaton.domain.model.{ContactType, Person}

case class PersonDetails(person: Person, contacts: List[ContactData])

object PersonDetails {
  implicit val codec: Codec[PersonDetails] = deriveCodec
}

case class ContactData(value: String, contactType: ContactType)

object ContactData {
  implicit val codec: Codec[ContactData] = deriveCodec
}

case class AddPersonRequest(name: String, contacts: List[ContactData])

object AddPersonRequest {
  implicit val codec: Codec[AddPersonRequest] = deriveCodec
}

trait PeopleService[F[_]] {

  def addPerson(request: AddPersonRequest): F[Either[ServiceError, PersonDetails]]

  def removePerson(id: UUID): F[Either[ServiceError, Unit]]

  def findAllPeople: F[Either[ServiceError, List[PersonDetails]]]

}
