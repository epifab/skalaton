package skalaton.domain.repositories

import java.util.UUID

import skalaton.domain.model.Address

trait AddressRepo[F[_]] {

  def findById(id: UUID): F[Option[Address]]

  def add(address: Address): F[Unit]

  def remove(personId: UUID): F[Unit]

}
