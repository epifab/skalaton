package skalaton.domain.repositories

import java.util.UUID

import skalaton.domain.model.Contact

trait ContactRepo[F[_]] {

  def findByIds(ids: List[UUID]): F[List[Contact]]

  def add(contact: Contact): F[Unit]

  def remove(personId: UUID): F[Unit]

}
