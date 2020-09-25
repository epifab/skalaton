package skalaton.frontend.bootstrap.table

import japgolly.scalajs.react.{CtorType, ScalaFnComponent}
import japgolly.scalajs.react.component.ScalaFn.{Component, Unmounted}
import japgolly.scalajs.react.vdom.Exports.HtmlTagOf
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.TableCell


object Table {

  case class Cell(content: TagMod, span: Int = 1)

  case class Props(headers: List[List[Cell]], data: List[List[Cell]])

  private def renderCells(el: HtmlTagOf[TableCell], cells: List[List[Cell]]): TagMod =
    cells.zipWithIndex.map { case (cols, rowIndex) =>
      <.tr(^.key := s"row-$rowIndex",
        cols.zipWithIndex.map { case (cell, colIndex) =>
          el(^.key := s"col-$colIndex",  ^.colSpan := cell.span, cell.content)
        }.toTagMod
      )
    }.toTagMod

  val component: Component[Props, CtorType.Props] = ScalaFnComponent[Props] { props =>
    <.table(^.className := "table",
      <.thead(renderCells(<.th, props.headers)),
      <.tbody(renderCells(<.td, props.data))
    )
  }

  def apply(headers: List[List[Cell]], data: List[List[Cell]]): Unmounted[Props] =
    component(Props(headers, data))

}
