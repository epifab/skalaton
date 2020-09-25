package skalaton.frontend.bootstrap.controls

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.component.{Scala, ScalaFn}
import japgolly.scalajs.react.{Callback, ScalaComponent}

import scala.util.Try

object DateInput {
  case class Props(value: Option[LocalDate], onChange: Option[LocalDate] => Callback, disabled: Boolean)
  case class State(value: String)

  private val dateFormat = "dd/MM/yyyy"
  private val dateFormatter = DateTimeFormatter.ofPattern(dateFormat)

  def format(date: LocalDate): String = dateFormatter.format(date)
  def parse(string: String): Option[LocalDate] = Try(LocalDate.parse(string, dateFormatter)).toOption

  class Backend($: BackendScope[Props, State]) {
    def render(props: Props, state: State): ScalaFn.Unmounted[TextInput.Props] =
      TextInput(
        value = Some(state.value),
        onChange = {
          case None => props.onChange(None)
          case Some(text) => $.modState(
            _.copy(value = text),
            parse(text).fold(Callback.empty)(date => props.onChange(Some(date)))
          )
        },
        placeholder = Some(dateFormat.toLowerCase),
        disabled = props.disabled,
        onBlur = $.modState(_.copy(value = props.value.map(format).getOrElse("")))
      )
  }

  private val component =
    ScalaComponent
      .builder[Props](getClass.getSimpleName)
      .initialStateFromProps(props => State(props.value.map(format).getOrElse("")))
      .renderBackend[Backend]
      .build

  def apply(value: Option[LocalDate], onChange: Option[LocalDate] => Callback, disabled: Boolean = false): Scala.Unmounted[Props, State, Backend] =
    component(Props(value, onChange, disabled))

}
