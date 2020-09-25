package skalaton.webapp.services

import java.util.UUID

import cats.data.EitherT
import cats.effect.Sync
import cats.implicits._
import cats.{Monad, MonadError}
import skalaton.domain.model.{Contact, Person}
import skalaton.domain.repositories.{ContactRepo, PersonRepo}
import skalaton.domain.services._

class PeopleServiceProvider[F[_]: Monad : Sync](personRepo: PersonRepo[F], contactRepo: ContactRepo[F])(implicit me: MonadError[F, Throwable]) extends PeopleService[F] {

  private def handleFailure[A](fallible: F[A]): EitherT[F, ServiceError, A] =
    EitherT(MonadError[F, Throwable].redeemWith(fallible)(
      error => Monad[F].pure(Left(ServiceError.InternalError(error.getMessage))),
      success => Monad[F].pure(Right(success))
    ))

  override def addPerson(request: AddPersonRequest): F[Either[ServiceError, PersonDetails]] =
    (for {
      id <- EitherT.right[ServiceError](Sync[F].delay(UUID.randomUUID))
      person = Person(id, request.name, request.dateOfBirth)
      contacts = request.contacts.map(cd => Contact(id, cd.value, cd.contactType))
      _ <- handleFailure(personRepo.add(person))
      _ <- handleFailure(contacts.map(contactRepo.add).sequence)
      personDetails = PersonDetails(person, contacts.map(c => ContactData(c.value, c.contactType)))
    } yield personDetails).value

  override def removePerson(id: UUID): F[Either[ServiceError, Unit]] =
    (for {
      person <- handleFailure(personRepo.findById(id))
      _ <- EitherT.fromEither[F](person match {
        case None => Left(ServiceError.NotFound)
        case Some(_) => Right(())
      })
      _ <- handleFailure(contactRepo.remove(id))
      _ <- handleFailure(personRepo.remove(id))
    } yield ()).value

  override def findAllPeople: F[Either[ServiceError, List[PersonDetails]]] =
    (for {
      people <- handleFailure(personRepo.findAll)
      contacts <- handleFailure(contactRepo.findByIds(people.map(_.id)))
      contactsById = contacts.groupBy(_.personId)
      peopleDetails = people.map(person =>
        PersonDetails(
          person,
          contactsById.getOrElse(person.id, List.empty).map(c => ContactData(c.value, c.contactType))
        )
      )
    } yield peopleDetails).value

}
