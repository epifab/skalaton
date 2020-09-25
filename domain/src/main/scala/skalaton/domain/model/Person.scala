package skalaton.domain.model

import java.util.UUID

import io.circe.Codec
import io.circe.generic.semiauto._

case class Person(id: UUID, name: String)

object Person {
  implicit val codec: Codec[Person] = deriveCodec
}
