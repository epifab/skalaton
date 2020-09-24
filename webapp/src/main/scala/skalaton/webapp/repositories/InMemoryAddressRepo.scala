package skalaton.webapp.repositories

import java.util.UUID

import cats.effect.IO
import skalaton.domain.model.Address
import skalaton.domain.repositories.AddressRepo

import scala.collection.mutable.ListBuffer

object InMemoryAddressRepo extends AddressRepo[IO] {

  private val addresses: ListBuffer[Address] = ListBuffer.empty

  override def findById(id: UUID): IO[Option[Address]] =
    IO.pure(addresses.find(_.personId == id))

  override def add(person: Address): IO[Unit] =
    IO.pure(addresses.addOne(person))

  override def remove(id: UUID): IO[Unit] =
    IO.pure(addresses.filterInPlace(_.personId != id))

}
