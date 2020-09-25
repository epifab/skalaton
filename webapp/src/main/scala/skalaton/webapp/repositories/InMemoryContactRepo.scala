package skalaton.webapp.repositories

import java.util.UUID

import cats.effect.IO
import skalaton.domain.model.Contact
import skalaton.domain.repositories.ContactRepo

import scala.collection.mutable.ListBuffer

object InMemoryContactRepo extends ContactRepo[IO] {

  private val contacts: ListBuffer[Contact] = ListBuffer.empty

  override def findByIds(ids: List[UUID]): IO[List[Contact]] =
    IO.pure(contacts.filter(c => ids.contains(c.personId)).toList)

  override def add(contact: Contact): IO[Unit] =
    IO.pure(contacts.addOne(contact))

  override def remove(id: UUID): IO[Unit] =
    IO.pure(contacts.filterInPlace(_.personId != id))

}
