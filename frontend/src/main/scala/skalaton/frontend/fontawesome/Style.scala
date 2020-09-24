package skalaton.frontend.fontawesome

abstract sealed class Style(style: String) {
  override def toString: String = style
}

object Style {
  case object Solid extends Style("fas")
  case object Brand extends Style("fab")
}