package skalaton.frontend.bootstrap.controls

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import skalaton.frontend.fontawesome.Icon

object Checkbox {

  case class Props(toggled: Boolean, onToggle: Boolean => Callback, label: String)

  private val component = ScalaFn[Props] { props =>
    <.div(^.cursor := "pointer",
      <.div(
        if (props.toggled) Icon.squareTick(onClick = Some(props.onToggle(false)))
        else Icon.square(onClick = Some(props.onToggle(true))),
        " ",
        props.label
      )
    )
  }

  def apply(toggled: Boolean, onToggle: Boolean => Callback, label: String): Unmounted[Props] =
    component(Props(toggled, onToggle, label))

}
