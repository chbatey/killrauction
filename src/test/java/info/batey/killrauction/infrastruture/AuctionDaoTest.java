package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AuctionDaoTest {
    private static Cluster cluster;
    private static Session session;

    private AuctionDao underTest;

    @BeforeClass
    public static void schemaSetup() throws Exception {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS killrauction_tests WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
        session.execute("use killrauction_tests");
        session.execute("CREATE TABLE IF NOT EXISTS auctions ( name text, ends bigint static, bid_time timeuuid, bid_user text, bid_amount bigint, primary KEY (name, bid_amount, bid_time ) ) WITH CLUSTERING ORDER BY (bid_amount DESC )");
    }

    @AfterClass
    public static void shutdown() throws Exception {
        session.execute("drop keyspace killrauction_tests");
        session.close();
        cluster.close();
    }

    @Before
    public void setUp() throws Exception {
        session.execute("truncate auctions");
        underTest = new AuctionDao(session);
        underTest.prepareStatements();
    }

    @Test
    public void createAndRetrieveDao() throws Exception {
        Auction expectedAuction = new Auction("ipad", Instant.now());

        underTest.createAuction(expectedAuction);
        Auction actualAuction = underTest.getAuction(expectedAuction.getName()).orElseThrow(() -> new AssertionError("Expected Auction not found"));

        assertEquals(expectedAuction.getName(), actualAuction.getName());
        assertEquals(expectedAuction.getEnds(), actualAuction.getEnds());
        assertEquals(expectedAuction.getBids(), actualAuction.getBids());
    }

    @Test
    public void addBid() throws Exception {
        Auction expectedAuction = new Auction("ipad", Instant.now());
        underTest.createAuction(expectedAuction);

        underTest.placeBid(expectedAuction.getName(), "chbatey", 101l);
        Auction actualAuction = underTest.getAuction(expectedAuction.getName()).orElseThrow(() -> new AssertionError("Expected Auction not found"));

        assertEquals(expectedAuction.getName(), actualAuction.getName());
        assertEquals(expectedAuction.getEnds(), actualAuction.getEnds());
        assertEquals(Arrays.asList(new BidVo("chbatey", 101l)), actualAuction.getBids());
    }

    //todo: bid for a user that does not exist
}