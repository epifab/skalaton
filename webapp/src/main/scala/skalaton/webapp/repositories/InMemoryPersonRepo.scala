package skalaton.webapp.repositories

import java.util.UUID

import cats.effect.IO
import skalaton.domain.model.Person
import skalaton.domain.repositories.PersonRepo
import scala.collection.mutable.ListBuffer

object InMemoryPersonRepo extends PersonRepo[IO] {

  private val people: ListBuffer[Person] = ListBuffer.empty

  override def findAll: IO[List[Person]] =
    IO.pure(people.toList)

  override def findById(id: UUID): IO[Option[Person]] =
    IO.pure(people.find(_.id == id))

  override def add(person: Person): IO[Unit] =
    IO.pure(people.addOne(person))

  override def remove(id: UUID): IO[Unit] =
    IO.pure(people.filterInPlace(_.id != id))

}
