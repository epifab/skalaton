package skalaton.webapp.routes

import java.time.LocalDate
import java.util.UUID

import skalaton.domain.services.{AddPersonRequest, ServiceError}
import skalaton.webapp.IntegrationTest
import skalaton.webapp.services.PeopleServiceApiClient

class PeopleApiSpec extends IntegrationTest {
  val client = new PeopleServiceApiClient(run)

  "New people can be created" in {
    val (person, people) = (for {
      person <- client.addPerson(AddPersonRequest("John Doe", LocalDate.of(1970, 1, 1), Some("N1")))
      people <- client.findAllPeople
    } yield person -> people).unsafeRunSync()

    people.getOrElse(fail("Could not get a list of people")) must contain(person.getOrElse(fail("The person could not be created")))
  }

  "Old people can be deleted" in {
    val (person, people) = (for {
      person <- client.addPerson(AddPersonRequest("John Doe", LocalDate.of(1970, 1, 1), Some("N1")))
      _      <- client.removePerson(person.getOrElse(fail("The person could not be added")).person.id)
      people <- client.findAllPeople
    } yield person -> people).unsafeRunSync()

    people.getOrElse(fail("Could not get a list of people")) must not contain person.getOrElse(fail("The person could not be created"))
  }

  "Non-existing people cannot be deleted" in {
    client.removePerson(UUID.randomUUID()).unsafeRunSync() mustBe Left(ServiceError.NotFound)
  }
}
