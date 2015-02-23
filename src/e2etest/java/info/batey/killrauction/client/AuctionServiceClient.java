package info.batey.killrauction.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class AuctionServiceClient {

    public static final AuctionServiceClient instance = new AuctionServiceClient();

    private Executor executor  = Executor.newInstance();
    private LastResponse lastResponse;

    private AuctionServiceClient() {}

    public void createUser(String userName, String password) throws IOException {
        StringEntity userEntry = new StringEntity(String.format(" {\"userName\": \"%s\", \"firstName\":\"Chris\", \"lastName\":\"Batey\", \"password\": \"%s\", \"email\":[\"christopher.batey@gmail.com\"] }", userName, password),
                ContentType.APPLICATION_JSON);
        lastResponse = new LastResponse(executor.execute(Request.Post("http://localhost:8080/api/user").body(userEntry)).returnResponse());
    }

    public void useUserNameAndPassword(String userName, String password) {
        executor = Executor.newInstance().auth(userName, password);
    }

    public void createAuction() throws IOException {
        lastResponse = new LastResponse(executor.execute(Request.Post("http://localhost:8080/api/auction")).returnResponse());
    }

    public LastResponse getLastResponse() {
        return lastResponse;
    }

    public void noUser() {
        executor = Executor.newInstance();
    }

    public static class LastResponse {

        private HttpResponse execute;

        public LastResponse(HttpResponse execute) {
            this.execute = execute;
        }

        public int statusCode() {
            return execute.getStatusLine().getStatusCode();
        }

        public String body() throws IOException {
            return EntityUtils.toString(execute.getEntity());
        }
    }

}
