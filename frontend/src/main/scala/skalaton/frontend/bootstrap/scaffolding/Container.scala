package skalaton.frontend.bootstrap.scaffolding

import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.html_<^._

object Container {

  case class Props(fluid: Boolean, content: TagMod)

  private val component = ScalaFn[Props] { props =>
    <.div(^.className := (if (props.fluid) "container-fluid" else "container"), props.content)
  }

  def fluid(content: TagMod*): Unmounted[Props] = component(Props(fluid = true, content.toTagMod))
  def apply(content: TagMod*): Unmounted[Props] = component(Props(fluid = false, content.toTagMod))

}
