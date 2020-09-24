package skalaton.frontend.bootstrap.controls

import japgolly.scalajs.react.vdom.TagMod

case class Item[+T](value: T, id: String, rendered: TagMod)
