package skalaton.domain.repositories

import java.util.UUID

import skalaton.domain.model.Person

trait PersonRepo[F[_]] {

  def findAll: F[List[Person]]

  def findById(id: UUID): F[Option[Person]]

  def add(person: Person): F[Unit]

  def remove(id: UUID): F[Unit]

}
