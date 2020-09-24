package skalaton.frontend.fontawesome

abstract class Size(size: String) {
  override def toString: String = size
}

object Size {
  case object Regular extends Size("1x")
  case object Lg extends Size("2x")
  case object Xl extends Size("3x")
  case object Xxl extends Size("4x")
}
