package skalaton.frontend.services

import java.util.UUID

import io.circe.syntax._
import skalaton.domain.services.{AddPersonRequest, PeopleService, PersonDetails, ServiceError}

object PeopleServiceConsumer extends PeopleService[ServiceCallback] {

  override def addPerson(request: AddPersonRequest): ServiceCallback[Either[ServiceError, PersonDetails]] =
    callback => ApiClient.send(Method.Post, s"/api/people", Some(request.asJson.noSpaces), ApiClient.jsonResponseHandler(callback))

  override def removePerson(id: UUID): ServiceCallback[Either[ServiceError, Unit]] =
    callback => ApiClient.send(Method.Delete, s"/api/people/$id", None, ApiClient.emptyResponseHandler(callback))

  override def findAllPeople: ServiceCallback[Either[ServiceError, List[PersonDetails]]] =
    callback => ApiClient.send(Method.Get, "/api/people", None, ApiClient.jsonResponseHandler(callback))

}
