package info.batey.killrauction

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

class UserServiceSimulation extends Simulation {
  val app: ConfigurableApplicationContext = SpringApplication.run(classOf[Application])
  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = app.stop()
  })

  val httpConf = http
    .baseURL("http://localhost:8080")
    .doNotTrackHeader("1")

  val scn = scenario("Create users with without conflicts")
    .repeat(1000, "n") {
      exec(http("create user ${n}")
        .post("/api/user")
        .body(StringBody(""" {"userName": "Chris${n}", "firstName":"Chris", "lastName":"Batey", "password": "password", "email":["christopher.batey@gmail.com"] }"""))
        .header("Content-Type", "application/json"))
  }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
