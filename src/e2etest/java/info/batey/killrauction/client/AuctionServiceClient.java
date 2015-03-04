package info.batey.killrauction.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private String host = "http://localhost:8080";
    private ObjectMapper objectMapper = new ObjectMapper();

    private AuctionServiceClient() {}

    public void createUser(String userName, String password) throws IOException {
        StringEntity userEntry = new StringEntity(String.format(" {\"userName\": \"%s\", \"firstName\":\"Chris\", \"lastName\":\"Batey\", \"password\": \"%s\", \"email\":[\"christopher.batey@gmail.com\"] }", userName, password),
                ContentType.APPLICATION_JSON);
        lastResponse = new LastResponse(executor.execute(Request.Post(host + "/api/user").body(userEntry)).returnResponse());
    }

    public void useUserNameAndPassword(String userName, String password) {
        executor = Executor.newInstance().auth(userName, password);
    }

    public HttpResponse createAuction(String auctionName) throws IOException {
        long expires = System.currentTimeMillis() + 10000;
        String auctionJson = String.format("{\"name\":\"%s\",\"end\":\"%d\"}", auctionName, expires );
        Response execute = executor.execute(Request.Post(host + "/api/auction").body(new StringEntity(auctionJson, ContentType.APPLICATION_JSON)));
        HttpResponse execute1 = execute.returnResponse();
        lastResponse = new LastResponse(execute1);
        return execute1;
    }

    public LastResponse getLastResponse() {
        return lastResponse;
    }

    public void noUser() {
        executor = Executor.newInstance();
    }

    public GetAuctionResponse getAuction(String auctionName) throws IOException {
        String body = EntityUtils.toString(executor.execute(Request.Get(host + "/api/auction/" + auctionName))
                .returnResponse().getEntity());
        return objectMapper.readValue(body, GetAuctionResponse.class);
    }

    public void reset() {
        executor  = Executor.newInstance();
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
