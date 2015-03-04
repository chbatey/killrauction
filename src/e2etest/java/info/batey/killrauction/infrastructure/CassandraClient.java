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

    public static final CassandraClient instance = new CassandraClient();

    private final Cluster cluster;
    private final Session session;

    private CassandraClient() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS killrauction WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
        session.execute("use killrauction");
        session.execute("CREATE TABLE IF NOT EXISTS users ( user_name text PRIMARY KEY, password text, first_name text , last_name text , emails set<text> )");
    }

    public void clearTables() {
        session.execute("truncate users;");
    }
}
