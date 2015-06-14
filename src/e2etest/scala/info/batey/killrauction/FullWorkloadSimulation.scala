package info.batey.killrauction

import java.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class FullWorkloadSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")
    .doNotTrackHeader("1")

  val seller = "gatling"
  val buyerOne = "buyer1"
  val buyerTwo = "buyer2"
  val password = "password"
  val auctionName = "Trousers"

  val rand: Random = new Random
  val salt: Long = rand.nextLong
  val n: Long = rand.nextLong()

  def createUser(userName: String) = exec(http("create_user").post("/api/user")
    .body(StringBody(s""" {"userName": "$userName", "salt": "$salt", "firstName":"Chris", "lastName":"Batey", "password": "$password", "email":["christopher.batey@gmail.com"] }"""))
    .header("Content-Type", "application/json")
  )

  def createAuction(name: String, userName: String, password: String) = exec(http("create_auction").post("/api/auction")
    .body(StringBody(s""" {"name": "$name", "end": "${System.currentTimeMillis() + 100000}" } """))
    .header("Content-Type", "application/json")
    .basicAuth(userName, password)
  )

  def bidRequest(user: String) = exec(http("bid_request").post("/api/auction/Trousers/bid")
    .body(StringBody(s""" {"name": "Trousers", "amount":"${n}" } """))
    .header("Content-Type", "application/json")
    .basicAuth(user, password)).pause(500 milliseconds)

  val sellerScenario = scenario("Seller")
      .exec(createUser(seller))
      .exec(createAuction(auctionName, seller, password))

  val buyer = scenario("Buyer one")
    .exec(createUser(buyerOne))
    .repeat(100, "n") {
      exec(bidRequest(buyerOne))
  }

  val buyer2 = scenario("Buyer two")
    .exec(createUser(buyerTwo))
    .repeat(100, "n") {
      exec(bidRequest(buyerTwo))
  }

  setUp(
    sellerScenario.inject(atOnceUsers(1)),
    buyer.inject(atOnceUsers(1)),
    buyer2.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
