package info.batey.killrauction

import java.util.Random

import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederWrapper
import io.gatling.http.Predef._
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

import scala.concurrent.duration._

class AuctionCreationSimulation extends Simulation {
  val app: ConfigurableApplicationContext = SpringApplication.run(classOf[Application])
  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = app.stop()
  })

  val httpConf = http
    .baseURL("http://localhost:8080")
    .doNotTrackHeader("1")
  
  val userName = "chris"
  val password = "password"
  val rand: Random = new Random
  val salt: Long = rand.nextLong

  val scn = scenario("Create Auction")
    .exec(http("create_user").post("/api/user")
      .body(StringBody(s""" {"userName": "$userName", "salt": "$salt", "firstName":"Chris", "lastName":"Batey", "password": "$password", "email":["christopher.batey@gmail.com"] }"""))
      .header("Content-Type", "application/json")
    )
    .repeat(100) {
      exec(http("request_1").post("/api/auction")
        .body(StringBody(""" {"name": "auction_name", "end":123456789 } """))
        .header("Content-Type", "application/json")
        .basicAuth(userName, password)).pause(10 milliseconds)
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
