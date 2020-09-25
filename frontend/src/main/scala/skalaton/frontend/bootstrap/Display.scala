package skalaton.frontend.bootstrap

sealed abstract class Display(id: String) {
  override def toString: String = id
}

case object Display {
  case object Block extends Display("block")
  case object Inline extends Display("inline")
}
