package info.batey.killrauction;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserCreationStepDefs {

    private Response response;
    private HttpHost localhost = new HttpHost("localhost", 8080);
    private Executor executor = Executor.newInstance().auth(localhost, "chris", "password").authPreemptive(localhost);


    @Given("^the user name does not exist$")
    public void the_user_name_does_not_exist() throws Throwable {
        executor = Executor.newInstance();
    }

    @When("^a user is created$")
    public void a_user_is_created() throws Throwable {
        StringEntity userEntry = new StringEntity(" {\"userName\": \"chbatey\", \"firstName\":\"Chris\", \"lastName\":\"Batey\", \"password\": \"banana\", \"email\":[\"christopher.batey@gmail.com\"] }", ContentType.APPLICATION_JSON);
        response = executor.execute(Request.Post("http://localhost:8080/api/user").body(userEntry));
    }

    @Then("^then the user creation is successful$")
    public void then_the_user_creation_is_successful() throws Throwable {
        HttpResponse httpResponse = response.returnResponse();
        assertThat(EntityUtils.toString(httpResponse.getEntity()), httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_CREATED));
    }
}
