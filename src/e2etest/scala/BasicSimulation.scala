import info.batey.killrauction.Application
import io.gatling.core.Predef._
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class BasicSimulation extends Simulation {
  val app: ConfigurableApplicationContext = SpringApplication.run(classOf[Application])
  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = app.stop()
  })

  val httpConf = http
    .baseURL("http://localhost:8080")
    .doNotTrackHeader("1")


  val scn = scenario("Create Auction") // feature is not really implemented yet
    .repeat(100) {
      exec(http("request_1").post("/api/auction").basicAuth("chris", "password")).pause(10 milliseconds)
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}