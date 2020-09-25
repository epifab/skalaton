package skalaton.webapp.repositories

import java.time.LocalDate
import java.util.UUID

import cats.effect.IO
import skalaton.domain.model.Person
import skalaton.domain.repositories.PersonRepo

import scala.collection.mutable.ListBuffer

object InMemoryPersonRepo extends PersonRepo[IO] {

  private val people: ListBuffer[Person] = ListBuffer.empty
  //  private val people: ListBuffer[Person] = ListBuffer(Person(
  //    UUID.fromString("dcf2a6de-e014-4158-851f-02125c7655c1"),
  //    "Luca Eto",
  //    LocalDate.of(1989, 9, 10)
  //  ))

  override def findAll: IO[List[Person]] =
    IO.pure(people.toList)

  override def findById(id: UUID): IO[Option[Person]] =
    IO.pure(people.find(_.id == id))

  override def add(person: Person): IO[Unit] =
    IO.pure(people.addOne(person))

  override def remove(id: UUID): IO[Unit] =
    IO.pure(people.filterInPlace(_.id != id))

}
