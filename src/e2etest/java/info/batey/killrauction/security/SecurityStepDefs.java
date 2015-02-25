package info.batey.killrauction.security;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import info.batey.killrauction.client.AuctionServiceClient;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SecurityStepDefs {

    @Given("^the user has not logged in$")
    public void the_user_has_not_logged_in() throws Throwable {
        AuctionServiceClient.instance.noUser();
    }

    @Given("^the user provides valid credentials$")
    public void the_user_provides_valid_credentials() throws Throwable {
        AuctionServiceClient.instance.createUser("chris", "secret");
        AuctionServiceClient.instance.useUserNameAndPassword("chris", "secret");
    }

    @Given("^the user provides invalid credentials$")
    public void the_user_provides_invalid_credentials() throws Throwable {
        AuctionServiceClient.instance.useUserNameAndPassword("rubbish", "rubbish");
    }

    @When("^an auction is created$")
    public void an_auction_is_created() throws Throwable {
        AuctionServiceClient.instance.createAuction("new_auction");
    }

    @Then("^the user is rejected as not authorized$")
    public void the_user_is_rejected_as_not_authorized() throws Throwable {
        AuctionServiceClient.LastResponse lastResponse = AuctionServiceClient.instance.getLastResponse();
        assertThat(lastResponse.body(), lastResponse.statusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Then("^then the auction is created$")
    public void then_the_auction_is_created() throws Throwable {
        AuctionServiceClient.LastResponse lastResponse = AuctionServiceClient.instance.getLastResponse();
        assertThat(lastResponse.body(), lastResponse.statusCode(), equalTo(HttpStatus.SC_CREATED));
    }
}
