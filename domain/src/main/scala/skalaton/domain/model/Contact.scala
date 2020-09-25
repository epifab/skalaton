package skalaton.domain.model

import java.util.UUID

import io.circe.{Codec, Decoder, Encoder}
import io.circe.generic.semiauto.deriveCodec

sealed trait ContactType

object ContactType {
  implicit val decoder: Decoder[ContactType] = Decoder[String].emap {
    case "email" => Right(Email)
    case "phone" => Right(Phone)
    case contactType => Left(s"Unknown contact type $contactType")
  }

  implicit val encoder: Encoder[ContactType] = Encoder[String].contramap {
    case Email => "email"
    case Phone => "phone"
  }

  case object Email extends ContactType
  case object Phone extends ContactType
}

case class Contact(personId: UUID, value: String, contactType: ContactType)

object Contact {
  implicit val codec: Codec[Contact] = deriveCodec
}
