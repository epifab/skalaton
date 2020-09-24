package skalaton.frontend.bootstrap.styles

sealed abstract class Style(id: String) {
  override def toString: String = id
}

object Style {
  case object Primary extends Style("primary")
  case object Secondary extends Style("secondary")
  case object Success extends Style("success")
  case object Info extends Style("info")
  case object Warning extends Style("warning")
  case object Danger extends Style("danger")
  case object Light extends Style("light")
}
