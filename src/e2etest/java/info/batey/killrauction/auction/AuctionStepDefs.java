package info.batey.killrauction.auction;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import info.batey.killrauction.client.AuctionServiceClient;

public class AuctionStepDefs {
    @Given("^the auction does not already exist$")
    public void the_auction_does_not_already_exist() throws Throwable {
    }

    @When("^a user creates an auction$")
    public void a_user_creates_an_auction() throws Throwable {
        AuctionServiceClient.instance.createAuction("new_auction");
    }

    @Then("^other users can see the auction$")
    public void other_users_can_see_the_auction() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }
}
