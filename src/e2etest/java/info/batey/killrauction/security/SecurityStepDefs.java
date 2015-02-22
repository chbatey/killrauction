package info.batey.killrauction.security;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import info.batey.killrauction.domain.AuctionUser;
import info.batey.killrauction.infrastructure.CassandraClient;
import info.batey.killrauction.web.user.UserCreate;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SecurityStepDefs {

    private Response response;
    private Executor executor;
    private CassandraClient cassandraClient = new CassandraClient();


    @Given("^the user has not logged in$")
    public void the_user_has_not_logged_in() throws Throwable {
        executor = Executor.newInstance();
    }

    @Given("^the user provides valid credentials$")
    public void the_user_provides_valid_credentials() throws Throwable {
        UserCreate realUser = new UserCreate("chris", "secret", "christopher", "batey", Collections.emptySet());
        cassandraClient.createAuctionUser(realUser);
        HttpHost localhost = new HttpHost("localhost", 8080);
        executor = Executor.newInstance().auth(localhost, "chris", "secret").authPreemptive(localhost);
    }

    @Given("^the user provides invalid credentials$")
    public void the_user_provides_invalid_credentials() throws Throwable {
        HttpHost localhost = new HttpHost("localhost", 8080);
        executor = Executor.newInstance().auth(localhost, "nota", "user").authPreemptive(localhost);
    }

    @When("^an auction is created$")
    public void an_auction_is_created() throws Throwable {
        response = executor.execute(Request.Post("http://localhost:8080/api/auction"));
    }

    @Then("^the user is rejected as not authorized$")
    public void the_user_is_rejected_as_not_authorized() throws Throwable {
        HttpResponse httpResponse = response.returnResponse();
        assertThat(EntityUtils.toString(httpResponse.getEntity()), httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Then("^then the auction is created$")
    public void then_the_auction_is_created() throws Throwable {
        HttpResponse httpResponse = response.returnResponse();
        assertThat(EntityUtils.toString(httpResponse.getEntity()), httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_CREATED));
    }
}
