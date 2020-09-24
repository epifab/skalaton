package skalaton.frontend.services

import io.circe.Decoder
import io.circe.parser.decode
import skalaton.domain.services.ServiceError
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.extra.Ajax
import org.scalajs.dom.XMLHttpRequest

import scala.util.chaining._

sealed abstract class Method private(val value: String)

object Method {
  case object Get extends Method("GET")
  case object Post extends Method("POST")
  case object Patch extends Method("PATCH")
  case object Put extends Method("PUT")
  case object Delete extends Method("DELETE")
}

object ApiClient {

  def send[Request, Response](method: Method, url: String, body: Option[String], handler: (XMLHttpRequest) => Callback): Callback =
    Ajax(method.value, url)
      .setRequestContentTypeJsonUtf8
      .pipe(r => body.fold(r.send)(r.send(_)))
      .onComplete(handler)
      .asCallback

  def jsonResponseHandler[Response](callback: Either[ServiceError, Response] => Callback)(response: XMLHttpRequest)(
    implicit
    decoder: Decoder[Response]
  ): Callback =
    response.status match {
      case success if success >= 200 && success < 300 =>
        val decoded = decode(response.responseText).getOrElse(throw new RuntimeException("Unable to decode the response"))
        callback(Right(decoded))
      case 0 => callback(Left(ServiceError.NetworkError))
      case _ => handleError(response, callback)
    }

  def emptyResponseHandler(callback: Either[ServiceError, Unit] => Callback)(response: XMLHttpRequest): Callback =
    response.status match {
      case success if success >= 200 && success < 300 => callback(Right(()))
      case _ => handleError(response, callback)
    }

  def handleError[Response](response: XMLHttpRequest, callback: Either[ServiceError, Response] => Callback): Callback = {
    response.status match {
      case 404 => callback(Left(ServiceError.NotFound))
      case _ => callback(Left(decode[ServiceError](response.responseText).getOrElse(ServiceError.InternalError(s"${response.status}: ${response.responseText}"))))
    }
  }

}
