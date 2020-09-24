package skalaton.frontend.bootstrap.controls

import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ReactEventFromInput, ReactKeyboardEvent}

object TextInput {
  case class Props(value: Option[String], onChange: Option[String] => Callback, onKeyPress: String => Callback, placeholder: Option[String], disabled: Boolean)

  private val component = ScalaFn[Props] { props =>
    <.input(
      ^.`type` := "text",
      ^.className := "form-control",
      ^.value := props.value.getOrElse(""),
      ^.disabled := props.disabled,
      ^.onChange ==> { (e: ReactEventFromInput) => Callback(e.preventDefault) >> props.onChange(Option(e.target.value).filter(_.nonEmpty)) },
      ^.onKeyPress ==> { event: ReactKeyboardEvent =>
        (if (event.key == "Enter") Callback(event.preventDefault()) else Callback.empty) >>
          props.onKeyPress(event.key)
      },
      props.placeholder.whenDefined(^.placeholder := _)
    )
  }

  def apply(value: Option[String], onChange: Option[String] => Callback, onKeyPress: String => Callback = _ => Callback.empty, placeholder: Option[String] = None, disabled: Boolean = false): Unmounted[Props] =
    component(Props(value, onChange, onKeyPress, placeholder, disabled))

}
