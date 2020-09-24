package skalaton.webapp.services

import java.time.LocalDate
import java.util.UUID

import cats.data.EitherT
import cats.{Monad, MonadError}
import cats.implicits._
import cats.effect.{IO, Sync}
import skalaton.domain.model.{Address, Person}
import skalaton.domain.repositories.{AddressRepo, PersonRepo}
import skalaton.domain.services.{AddPersonRequest, PeopleService, PersonDetails, ServiceError}

class PeopleServiceProvider[F[_]: Monad : Sync](personRepo: PersonRepo[F], addressRepo: AddressRepo[F])(implicit me: MonadError[F, Throwable]) extends PeopleService[F] {

  private def handleFailure[A](fallible: F[A]): EitherT[F, ServiceError, A] =
    EitherT(MonadError[F, Throwable].redeemWith(fallible)(
      error => Monad[F].pure(Left(ServiceError.InternalError(error.getMessage))),
      success => Monad[F].pure(Right(success))
    ))

  override def addPerson(request: AddPersonRequest): F[Either[ServiceError, PersonDetails]] =
    (for {
      id <- EitherT.right[ServiceError](Sync[F].delay(UUID.randomUUID))
      person = Person(id, request.name, request.dateOfBirth)
      address = request.postcode.map(postcode => Address(id, postcode))
      _ <- handleFailure(personRepo.add(person))
      _ <- handleFailure(address.fold(Monad[F].pure(()))(addressRepo.add))
      personDetails = PersonDetails(person, address)
    } yield personDetails).value

  override def removePerson(id: UUID): F[Either[ServiceError, Unit]] =
    (for {
      _ <- handleFailure(addressRepo.remove(id))
      _ <- handleFailure(personRepo.remove(id))
    } yield ()).value

  override def findAllPeople: F[Either[ServiceError, List[PersonDetails]]] =
    (for {
      people <- handleFailure(personRepo.findAll)
      peopleDetails <- handleFailure(people.map(person => addressRepo.findById(person.id).map(PersonDetails(person, _))).sequence)
    } yield peopleDetails).value

}
