package skalaton.frontend

import japgolly.scalajs.react.Callback

package object services {
  type ServiceCallback[A] = (A => Callback) => Callback
}
