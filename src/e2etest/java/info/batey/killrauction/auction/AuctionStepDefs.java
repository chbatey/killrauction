package info.batey.killrauction.auction;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import info.batey.killrauction.client.AuctionServiceClient;
import info.batey.killrauction.client.GetAuctionResponse;
import info.batey.killrauction.domain.BidVo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class AuctionStepDefs {

    private Optional<List<GetAuctionResponse>> allAuctions;

    @Given("^the auction does not already exist$")
    public void the_auction_does_not_already_exist() throws Throwable {
    }

    @When("^a user creates an auction$")
    public void a_user_creates_an_auction() throws Throwable {
        HttpResponse response = AuctionServiceClient.instance.createAuction("new_auction");
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Then("^other users can see the auction$")
    public void other_users_can_see_the_auction() throws Throwable {
        GetAuctionResponse newAuction = AuctionServiceClient.instance.getAuction("new_auction").get();
        assertNotNull(newAuction);
        assertEquals("new_auction", newAuction.getName());
    }

    @Given("^all requests are made with a valid user$")
    public void all_requests_are_made_with_a_valid_user() throws Throwable {
        AuctionServiceClient.instance.createUser("username", "password");
        AuctionServiceClient.instance.useUserNameAndPassword("username", "password");
    }

    @Given("^an auction exists for an ipad$")
    public void an_auction_exists_for_an_ipad() throws Throwable {
        AuctionServiceClient.instance.createAuction("ipad");
    }

    @When("^a user makes a bid$")
    public void a_user_makes_a_bid() throws Throwable {
        AuctionServiceClient.instance.placeBid("ipad", 100);
    }

    @Then("^the bid is accepted$")
    public void the_bid_is_accepted() throws Throwable {
        AuctionServiceClient.LastResponse lastResponse = AuctionServiceClient.instance.getLastResponse();
        assertThat(lastResponse.statusCode(), equalTo(HttpStatus.SC_CREATED));
    }

    @And("^the bid is viewable by others$")
    public void the_bid_is_viewable_by_others() throws Throwable {
        Optional<GetAuctionResponse> bids = AuctionServiceClient.instance.getAuction("ipad");
        assertTrue("No bids returned", bids.isPresent());
        assertThat(bids.get().getBids(), hasItems(new BidVo("username", 100l)));
    }

    @Given("^multiple auctions exist$")
    public void multiple_auctions_exist() throws Throwable {
        AuctionServiceClient.instance.createAuction("one", 1);
        AuctionServiceClient.instance.createAuction("two", 1);
        AuctionServiceClient.instance.createAuction("three", 1);
    }

    @When("^a user requests all auctions$")
    public void a_user_requests_all_auctions() throws Throwable {
        allAuctions = AuctionServiceClient.instance.getAllAuctions();
    }

    @Then("^the auctions are returned$")
    public void the_auctions_are_returned() throws Throwable {
        assertThat("Expected auctions to be returned", allAuctions.isPresent(), equalTo(true));
        List<GetAuctionResponse> getAuctionResponses = allAuctions.get();
        assertThat(getAuctionResponses.size(), equalTo(3));
        assertThat(getAuctionResponses, hasItems(
                new GetAuctionResponse("three", 1, Collections.emptyList()),
                new GetAuctionResponse("one", 1, Collections.emptyList()),
                new GetAuctionResponse("two", 1, Collections.emptyList())));
    }

}
