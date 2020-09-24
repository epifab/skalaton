package skalaton.frontend.bootstrap.controls

import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ReactEventFromInput}

object Dropdown {

  case class Props[T](selected: Option[T], options: Seq[Item[T]], onChange: Item[T] => Callback)

  private def component[T] = ScalaFn[Props[T]] { props =>
    <.select(
      ^.className := "form-control",
      props.selected
        .flatMap(value => props.options.find(item => item.value == value))
        .whenDefined(^.value := _.id),
      ^.onChange ==> { (event: ReactEventFromInput) =>
        Callback(event.preventDefault) >> props.options.find(_.id == event.target.value).fold(Callback.empty)(props.onChange)
      },
      props.options.map(option =>
        <.option(
          ^.key := option.id,
          ^.value := option.id,
          option.rendered
        )
      ).toTagMod
    )
  }

  def apply[T](selected: Option[T], options: Seq[Item[T]], onChange: Item[T] => Callback): Unmounted[Props[T]] =
    component[T](Props(selected, options, onChange))

}
