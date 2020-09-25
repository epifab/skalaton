package skalaton.frontend.people

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.vdom.html_<^._
import skalaton.domain.services.{ContactData, PersonDetails}
import skalaton.frontend.PageContext
import skalaton.frontend.bootstrap.controls.TextInput

object PersonForm {
  case class Props(pageContext: PageContext, person: Option[PersonDetails])
  case class State(name: Option[String], contacts: List[ContactData])

  class Backend($: BackendScope[Props, State]) {
    def render(state: State, props: Props) =
      <.form(
        <.div(^.className := "form-group",
          TextInput(
            state.name,
            onChange = name => $.modState(_.copy(name = name)),
            placeholder = Some("Person name")
          )
        )
      )
  }

  private val component =
    ScalaComponent
      .builder[Props](getClass.getSimpleName)
      .initialStateFromProps(props => State(
        name = props.person.map(_.person.name),
        contacts = props.person.map(_.contacts).getOrElse(List.empty)
      ))
      .renderBackend[Backend]
      .build

  def apply(pageContext: PageContext, person: Option[PersonDetails]) =
    component(Props(pageContext, person))

}
