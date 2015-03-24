package info.batey.killrauction

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class FullWorkloadSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")
    .doNotTrackHeader("1")

  val userName = "gatling"
  val password = "password"
  val auctionName = "Trousers"

  val createUser = exec(http("create_user").post("/api/user")
    .body(StringBody(s""" {"userName": "$userName", "firstName":"Chris", "lastName":"Batey", "password": "$password", "email":["christopher.batey@gmail.com"] }"""))
    .header("Content-Type", "application/json")
  )

  val createAuction = exec(http("create auction").post("/api/auction")
    .body(StringBody(s""" {"name": "$auctionName", "end":123456789 } """))
    .header("Content-Type", "application/json")
    .basicAuth(userName, password)
  )

  val scn = scenario("E2E Scenario")
    .exec(createUser)
    .exec(createAuction)
    .repeat(100, "n") {
      exec(http("bid $n").post("/api/auction/Trousers/bid")
        .body(StringBody(""" {"name": "Trousers", "amount":${n} } """))
        .header("Content-Type", "application/json")
        .basicAuth(userName, password)).pause(500 milliseconds)
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
