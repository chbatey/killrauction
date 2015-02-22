package info.batey.killrauction.infrastructure;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.base.Charsets;
import info.batey.killrauction.domain.AuctionUser;
import info.batey.killrauction.web.user.UserCreate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CassandraClient {
    private final Cluster cluster;
    private final Session session;
    private MessageDigest digest;
    private Base64.Encoder base64 = Base64.getEncoder();

    public CassandraClient() {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Oh no");
        }
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS killrauction WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
        session.execute("use killrauction");
        session.execute("CREATE TABLE IF NOT EXISTS users ( user_name text PRIMARY KEY, password text, first_name text , last_name text , emails set<text> )");
    }

    public void createAuctionUser(UserCreate auctionUser) {
        String encodedPassword = base64.encodeToString(digest.digest(auctionUser.getPassword().getBytes(Charsets.UTF_8)));
        session.execute("INSERT INTO users (user_name, password, first_name , last_name , emails ) values (?, ?, ?, ?, ?) if not exists",
                auctionUser.getUserName(), auctionUser.getPassword(), auctionUser.getFirstName(),
                auctionUser.getLastName(), auctionUser.getEmails());
    }
}
