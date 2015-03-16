package info.batey.killrauction.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AuctionServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionServiceClient.class);

    public static final AuctionServiceClient instance = new AuctionServiceClient();

    private Executor executor = Executor.newInstance();
    private LastResponse lastResponse;
    private String host = "http://localhost:8080";
    private ObjectMapper objectMapper = new ObjectMapper();

    private AuctionServiceClient() {
    }

    public void createUser(String userName, String password) throws IOException {
        StringEntity userEntry = new StringEntity(String.format(" {\"userName\": \"%s\", \"firstName\":\"Chris\", \"lastName\":\"Batey\", \"password\": \"%s\", \"email\":[\"christopher.batey@gmail.com\"] }", userName, password),
                ContentType.APPLICATION_JSON);
        lastResponse = new LastResponse(executor.execute(Request.Post(host + "/api/user").body(userEntry)).returnResponse());
    }

    public void useUserNameAndPassword(String userName, String password) {
        executor = Executor.newInstance().auth(userName, password);
    }

    public HttpResponse createAuction(String auctionName) throws IOException {
        return createAuction(auctionName, System.currentTimeMillis() + 1000);
    }

    public HttpResponse createAuction(String auctionName, long expires) throws IOException {
        String auctionJson = String.format("{\"name\":\"%s\",\"end\":\"%d\"}", auctionName, expires);
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

    public Optional<GetAuctionResponse> getAuction(String auctionName) throws IOException {
        HttpResponse httpResponse = executor.execute(Request.Get(host + "/api/auction/" + auctionName))
                .returnResponse();
        String body = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            return Optional.of(objectMapper.readValue(body, GetAuctionResponse.class));
        } else {
            LOGGER.info("GetAuction returned non-200 response code {} body {}", httpResponse.getStatusLine().getStatusCode(), body);
            return Optional.empty();
        }
    }

    public void reset() {
        executor = Executor.newInstance();
    }

    public void placeBid(String auction, int amountInPence) throws IOException {
        String auctionJson = String.format("{\"name\":\"%s\",\"amount\":\"%d\"}", auction, amountInPence);
        Response execute = executor.execute(Request.Post(host + "/api/auction/" + auction + "/bid").body(new StringEntity(auctionJson, ContentType.APPLICATION_JSON)));
        HttpResponse execute1 = execute.returnResponse();
        lastResponse = new LastResponse(execute1);
    }

    public Optional<List<GetAuctionResponse>> getAllAuctions() throws IOException {
        HttpResponse httpResponse = executor.execute(Request.Get(host + "/api/auction"))
                .returnResponse();
        String body = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            return Optional.of(objectMapper.readValue(body, GetAuctionResponse[].class)).map(Arrays::asList);
        } else {
            LOGGER.info("GetAllAuction returned non-200 response code {} body {}", httpResponse.getStatusLine().getStatusCode(), body);
            return Optional.empty();
        }
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
