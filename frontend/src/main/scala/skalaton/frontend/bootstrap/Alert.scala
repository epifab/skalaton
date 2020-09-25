package skalaton.frontend.bootstrap

import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.preventDefault
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.html_<^._
import skalaton.frontend.bootstrap.styles.Style
import skalaton.frontend.fontawesome.Icon

object Alert {
  case class Props(style: Style, dismissible: Boolean, cssClasses: List[String] = List.empty)

  private def component(content: TagMod) = ScalaFn[Props] { props =>
    <.div(
      ^.className := (List("alert", s"alert-${props.style}") ++ props.cssClasses).mkString(" "),
      content,
      TagMod.when(props.dismissible) {
        <.button(
          ^.className := "close",
          VdomAttr("data-dismiss") := "alert",
          VdomAttr("aria-label") := "Close",
          <.span(VdomAttr("aria-hidden") := "true", Icon.times()),
          ^.onClick ==> preventDefault
        )
      }
    )
  }

  def apply(style: Style, dismissible: Boolean = false, cssClasses: List[String] = List.empty)(content: TagMod*): Unmounted[Props] =
    component(content.toTagMod)(Props(style, dismissible, cssClasses))
}
