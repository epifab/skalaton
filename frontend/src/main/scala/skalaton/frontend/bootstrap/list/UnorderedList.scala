package skalaton.frontend.bootstrap.list

import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.html_<^._

object UnorderedList {
  case class Props(items: List[TagMod], cssClasses: List[String])

  private val component = ScalaFn[Props] { props =>
    <.ul(^.className := (List("list-unstyled") ++ props.cssClasses).mkString(" "),
      props.items.zipWithIndex.map { case (content, index) => <.li(^.key := s"li-$index", content)}.toTagMod
    )
  }

  def apply(cssClasses: List[String])(content: List[TagMod]): TagMod = {
    if (content.isEmpty) TagMod.empty
    else component(Props(content.toList, cssClasses))
  }

  def apply(content: List[TagMod]): TagMod = apply(cssClasses = List.empty)(content)

}
