package skalaton.domain.services

import java.time.LocalDate
import java.util.UUID

import skalaton.domain.model.{Address, Person}

case class PersonDetails(person: Person, address: Option[Address])

trait PeopleService[F[_]] {

  def addPerson(name: String, dateOfBirth: LocalDate, postcode: Option[String]): F[Either[ServiceError, PersonDetails]]

  def removePerson(id: UUID): F[Either[ServiceError, Unit]]

  def findAllPeople: F[Either[ServiceError, List[PersonDetails]]]

}
