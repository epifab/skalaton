package skalaton.domain.services

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, JsonObject}

sealed abstract class ServiceError(val code: String, val details: Option[String] = None)

object ServiceError {
  case object NotAuthenticated extends ServiceError("not-authenticated") {
    override def toString: String = "Not authenticated"
  }
  case object SessionExpired extends ServiceError("session-expired") {
    override def toString: String = "Session expired"
  }
  case object SessionCorrupted extends ServiceError("session-corrupted") {
    override def toString: String = "Session corrupted"
  }
  case object Forbidden extends ServiceError("forbidden") {
    override def toString: String = "Forbidden"
  }
  case object NotFound extends ServiceError("not-found") {
    override def toString: String = "Not found"
  }
  case class BadRequest(reason: String) extends ServiceError("bad-request", Some(reason)) {
    override def toString: String = s"Bad request: $reason"
  }
  case object NetworkError extends ServiceError("network-error", Some("Network error")) {
    override def toString: String = "Network error"
  }
  case class InternalError(reason: String) extends ServiceError("internal-error", Some(reason)) {
    override def toString: String = s"Internal service error: $reason"
  }

  implicit val encoder: Encoder[ServiceError] = error =>
    Encoder.encodeJsonObject(
      JsonObject(
        "code" -> error.code.asJson,
        "details" -> error.details.asJson
      )
    )

  implicit val decoder: Decoder[ServiceError] = (c: HCursor) =>
    c.downField("code").as[String].flatMap {
      case "not-authenticated" => Right(NotAuthenticated)
      case "session-corrupted" => Right(SessionCorrupted)
      case "session-expired" => Right(SessionExpired)
      case "forbidden" => Right(Forbidden)
      case "not-found" => Right(NotFound)
      case "internal-error" => c.downField("details").as[String].map(InternalError)
      case "invalid-request" => c.downField("details").as[String].map(BadRequest)
    }
}
