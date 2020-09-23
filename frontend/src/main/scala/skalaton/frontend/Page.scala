package skalaton.frontend

import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div

case class PageContext(router: RouterCtl[Page])

sealed trait Page {
  def key: String
  def title: String
  def render(context: PageContext): TagMod
}

object Page {

  case object Home extends Page {
    override def key: String = "home"
    override val title: String = "Home"
    override def render(context: PageContext): TagMod =
      <.div(^.className := "container",
        <.h1("Welcome to Skalaton!"),
        <.div(
          <.p("Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
            " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s," +
            " when an unknown printer took a galley of type and scrambled it to make a type specimen book." +
            " It has survived not only five centuries, but also the leap into electronic typesetting," +
            " remaining essentially unchanged." +
            " It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages," +
            " and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
          )
        )
      )
  }

}

object PageWrapper {

  def apply(routerCtl: RouterCtl[Page], page: Page): VdomTagOf[Div] = {
    org.scalajs.dom.document.title = page.title
    <.div(^.className := s"page-wrapper ${page.key}-page", page.render(PageContext(routerCtl)))
  }

}
