package skalaton.frontend.bootstrap

sealed abstract class Size(id: String) {
  override def toString: String = id
}

object Size {
  case object Large extends Size("lg")
  case object Medium extends Size("md")
}
