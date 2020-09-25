package skalaton.frontend

import japgolly.scalajs.react.CtorType
import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.{Component, Unmounted}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import skalaton.frontend.bootstrap.scaffolding.Container
import skalaton.frontend.people.{PeopleTable, PersonForm}

case class PageContext(router: RouterCtl[Page])

sealed trait Page {
  def key: String
  def title: String
  def render(context: PageContext): TagMod
}

object Page {

  case object PeopleListing extends Page {
    override def key: String = "home"
    override val title: String = "Home"
    override def render(context: PageContext): TagMod = {
      Container(
        <.h1("Welcome to Skalaton!"),
        PeopleTable(context)
      )
    }
  }

  case object NewPersonForm extends Page {
    override def key: String = "new-person"
    override def title: String = "Add a new person"
    override def render(context: PageContext): TagMod =
      Container(
        <.h1("Create a new person"),
        PersonForm(context, None)
      )

  }

}

object PageWrapper {

  val component: Component[(RouterCtl[Page], Page), CtorType.Props] = ScalaFn[(RouterCtl[Page], Page)] { case (router, page) =>
    <.div(^.className := s"page-wrapper ${page.key}-page", page.render(PageContext(router)))
  }

  def apply(router: RouterCtl[Page], page: Page): Unmounted[(RouterCtl[Page], Page)] = {
    org.scalajs.dom.document.title = page.title
    component(router, page)
  }

}
