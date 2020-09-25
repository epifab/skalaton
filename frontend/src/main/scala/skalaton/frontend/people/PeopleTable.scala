package skalaton.frontend.people

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.{BackendScope, Unmounted}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Element
import skalaton.domain.model.ContactType.{Email, Phone}
import skalaton.domain.services.{ContactData, PersonDetails, ServiceError}
import skalaton.frontend.Page.NewPersonForm
import skalaton.frontend.PageContext
import skalaton.frontend.bootstrap.list.UnorderedList
import skalaton.frontend.bootstrap.table.Table
import skalaton.frontend.bootstrap.{Alert, Style}
import skalaton.frontend.fontawesome.{Icon, Size}
import skalaton.frontend.services.PeopleServiceConsumer


object PeopleTable {
  case class State(people: Option[Either[ServiceError, List[PersonDetails]]])
  case class Props(context: PageContext)

  class Backend($: BackendScope[Props, State]) {
    def render(state: State, props: Props): VdomTagOf[Element] = state.people match {
      case None =>
        <.article(Icon.spin(size = Size.Lg))

      case Some(Left(error)) =>
        <.article(Alert(style = Style.Danger)(s"Something went wrong: $error"))

      case Some(Right(people)) =>
        <.article(
          Table(
            headers = List(List(
              Table.Cell("Name"),
              Table.Cell("Contacts"),
              Table.Cell(Icon.plus(onClick =
                Some(props.context.router.set(NewPersonForm))
              ))
            )),
            data = people.map(person =>
              List(
                Table.Cell(person.person.name),
                Table.Cell(
                  UnorderedList(person.contacts.map {
                    case ContactData(email, Email) => <.p(Icon.email(), " ", email)
                    case ContactData(phone, Phone) => <.p(Icon.phone(), " ", phone)
                  })
                ),
                Table.Cell(
                  Icon.trash()
                )
              )
            )
          )
        )
    }
  }

  private val component =
    ScalaComponent
      .builder[Props](getClass.getSimpleName)
      .initialState(State(None))
      .renderBackend[Backend]
      .componentDidMount(component => PeopleServiceConsumer.findAllPeople(results => component.setState(State(Some(results)))))
      .build

  def apply(pageContext: PageContext): Unmounted[Props, State, Backend] =
    component(Props(pageContext))

}
