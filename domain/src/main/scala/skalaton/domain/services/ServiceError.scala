package skalaton.domain.services

sealed trait ServiceError

object ServiceError {
  case object NotFound extends ServiceError
  case class BadRequest(reason: String) extends ServiceError
  case class InternalError(reason: String) extends ServiceError
}