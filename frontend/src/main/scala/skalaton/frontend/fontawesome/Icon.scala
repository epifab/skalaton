package skalaton.frontend.fontawesome

import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ReactEvent}
import org.scalajs.dom.html.Element


class Icon(name: String, style: Style = Style.Solid, spin: Boolean = false) {
  def apply(size: Size = Size.Regular, onClick: Option[Callback] = None): VdomTagOf[Element] =
    <.i(^.className := (List(s"fa-$name", style.toString, s"fa-$size") ++ (if (spin) List("fa-spin") else List.empty)).mkString(" "),
      onClick.whenDefined { onClick => TagMod(
        ^.onClick ==> ((event: ReactEvent) => onClick),
        ^.cursor := "pointer"
      )}
    )
}

object Icon {

  val search = new Icon("search")

  val plus = new Icon("plus")

  val times = new Icon("times")

  val trash = new Icon("trash")

  val tick = new Icon("check")

  val square = new Icon("square")

  val squareTick = new Icon("check-square")

  val phone = new Icon("phone")

  val email = new Icon("envelope")

  val spin = new Icon("spinner", spin = true)

}
