package skalaton.domain.model

import java.util.UUID

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Address(personId: UUID, postcode: String)

object Address {
  implicit val codec: Codec[Address] = deriveCodec
}
