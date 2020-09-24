package skalaton.webapp.services

import java.time.LocalDate
import java.util.UUID

import cats.Monad
import cats.implicits._
import cats.effect.{IO, Sync}
import skalaton.domain.model.{Address, Person}
import skalaton.domain.repositories.{AddressRepo, PersonRepo}
import skalaton.domain.services.{PeopleService, PersonDetails, ServiceError}

class BackendPeopleService[F[_]: Monad : Sync](personRepo: PersonRepo[F], addressRepo: AddressRepo[F]) extends PeopleService[F] {

  override def addPerson(name: String, dateOfBirth: LocalDate, postcode: Option[String]): F[Either[ServiceError, PersonDetails]] =
    for {
      id <- Sync[F].delay(UUID.randomUUID)
      person = Person(id, name, dateOfBirth)
      address = postcode.map(postcode => Address(id, postcode))
      _ <- personRepo.add(person)
      _ <- address.fold(Monad[F].pure(()))(addressRepo.add)
    } yield Right(PersonDetails(person, address))

  override def removePerson(id: UUID): F[Either[ServiceError, Unit]] =
    for {
      _ <- addressRepo.remove(id)
      _ <- personRepo.remove(id)
    } yield Right(())

  override def findAllPeople: F[Either[ServiceError, List[PersonDetails]]] =
    for {
      people <- personRepo.findAll
      peopleDetails <- people.map(person => addressRepo.findById(person.id).map(PersonDetails(person, _))).sequence
    } yield Right(peopleDetails)

}
