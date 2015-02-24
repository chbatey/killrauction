package info.batey.killrauction.user;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import info.batey.killrauction.client.AuctionServiceClient;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserCreationStepDefs {

    @Given("^the user name does not exist$")
    public void the_user_name_does_not_exist() throws Throwable {
        // no-op
    }

    @When("^a user is created$")
    public void a_user_is_created() throws Throwable {
        AuctionServiceClient.instance.createUser("new_user", "password");
    }

    @Then("^then the user creation is successful$")
    public void then_the_user_creation_is_successful() throws Throwable {
        AuctionServiceClient.LastResponse lastResponse = AuctionServiceClient.instance.getLastResponse();
        assertThat(lastResponse.body(), lastResponse.statusCode(), equalTo(HttpStatus.SC_CREATED));
    }

    @And("^that user logs in$")
    public void that_user_logs_in() throws Throwable {
        // Express the Regexp above with the code you wish you had
        AuctionServiceClient.instance.useUserNameAndPassword("new_user", "password");
    }
}
