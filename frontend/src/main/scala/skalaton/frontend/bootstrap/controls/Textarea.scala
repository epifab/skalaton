package skalaton.frontend.bootstrap.controls

import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ReactEventFromInput, ReactKeyboardEvent}

object Textarea {
  case class Props(value: Option[String], onChange: Option[String] => Callback, rows: Int, placeholder: Option[String], disabled: Boolean)

  private val component = ScalaFn[Props] { props =>
    <.textarea(
      ^.className := "form-control",
      props.placeholder.whenDefined(^.placeholder := _),
      ^.disabled := props.disabled,
      ^.rows := props.rows,
      ^.onChange ==> { (e: ReactEventFromInput) =>
        Callback(e.preventDefault) >>
          props.onChange(Option(e.target.value).filter(_.nonEmpty))
      },
      ^.onKeyPress ==> { event: ReactKeyboardEvent =>
        if (event.key == "Enter") Callback(event.preventDefault())
        else Callback.empty
      },
      props.value.whenDefined,
    )
  }

  def apply(value: Option[String], onChange: Option[String] => Callback, rows: Int, placeholder: Option[String] = None, disabled: Boolean = false): Unmounted[Props] =
    component(Props(value, onChange, rows, placeholder, disabled))

}
