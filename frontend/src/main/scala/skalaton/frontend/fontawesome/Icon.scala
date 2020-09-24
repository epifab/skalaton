package skalaton.frontend.fontawesome

import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ReactEvent}
import org.scalajs.dom.html.Element


class Icon(name: String, style: Style = Style.Solid) {
  def apply(size: Size = Size.Regular, onClick: Callback = Callback.empty): VdomTagOf[Element] =
    <.i(^.className := s"fa-$name fa-$style fa-$size",
      ^.onClick ==> ((event: ReactEvent) => Callback(event.preventDefault()) >> onClick)
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

}
