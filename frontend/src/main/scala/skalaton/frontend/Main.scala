package skalaton.frontend

import japgolly.scalajs.react.extra.router.{BaseUrl, Router, RouterConfigDsl}
import japgolly.scalajs.react.extra.router.SetRouteVia.HistoryReplace
import org.scalajs.dom.document

object Main extends App {
  private val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (emptyRule
      | staticRoute("/", Page.PeopleListing) ~> renderR(PageWrapper(_, Page.PeopleListing))
      | staticRoute("/new-user", Page.NewPersonForm) ~> renderR(PageWrapper(_, Page.NewPersonForm))
      ).notFound(redirectToPage(Page.PeopleListing)(HistoryReplace))
  }

  private val router = Router(BaseUrl.fromWindowOrigin, routerConfig)

  router().renderIntoDOM(document.getElementById("app-wrapper"))

}
