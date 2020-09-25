package skalaton.frontend.people

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Element
import skalaton.domain.services.{PersonDetails, ServiceError}
import skalaton.frontend.bootstrap.Alert
import skalaton.frontend.bootstrap.styles.Style
import skalaton.frontend.bootstrap.table.Table
import skalaton.frontend.fontawesome.{Icon, Size}
import skalaton.frontend.services.PeopleServiceConsumer


object PeopleTable {
  case class State(people: Option[Either[ServiceError, List[PersonDetails]]])

  class Backend($: BackendScope[Unit, State]) {
    def render(state: State): VdomTagOf[Element] = state.people match {
      case None =>
        <.article(Icon.spin(size = Size.Lg))

      case Some(Left(error)) =>
        <.article(Alert(style = Style.Danger)(s"Something went wrong: $error"))

      case Some(Right(people)) =>
        <.article(
          Table(
            headers = List(List(
              Table.Cell("Name"),
              Table.Cell("Postcode")
            )),
            data = people.map(person =>
              List(
                Table.Cell(person.person.name),
                Table.Cell(person.address.fold("<No address>")(_.postcode))
              )
            )
          )
        )
    }
  }

  private val component =
    ScalaComponent
      .builder[Unit](getClass.getSimpleName)
      .initialState(State(None))
      .renderBackend[Backend]
      .componentDidMount(component => PeopleServiceConsumer.findAllPeople(results => component.setState(State(Some(results)))))
      .build

  def apply(): Scala.Unmounted[Unit, State, Backend] = component()

}
