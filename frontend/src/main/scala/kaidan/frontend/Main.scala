package kaidan.frontend

import japgolly.scalajs.react.extra.router.{BaseUrl, Router, RouterConfigDsl}
import japgolly.scalajs.react.extra.router.SetRouteVia.HistoryReplace
import org.scalajs.dom.document

object Main extends App {
  private val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (emptyRule
      | staticRoute("/", Page.Home) ~> renderR(PageWrapper(_, Page.Home))
      ).notFound(redirectToPage(Page.Home)(HistoryReplace))
  }

  private val router = Router(BaseUrl.fromWindowOrigin, routerConfig)

  router().renderIntoDOM(document.getElementById("app-wrapper"))

}
