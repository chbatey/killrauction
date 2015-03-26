package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        session.execute("CREATE TABLE IF NOT EXISTS auction_bids ( name text, bid_time timeuuid, bid_user text, bid_amount bigint, primary KEY (name, bid_amount, bid_time ) ) WITH CLUSTERING ORDER BY (bid_amount DESC )");
        session.execute("CREATE TABLE IF NOT EXISTS auctions ( name text primary key, owner text, ends bigint);");
    }

    @AfterClass
    public static void shutdown() throws Exception {
//        session.execute("drop keyspace killrauction_tests");
        session.close();
        cluster.close();
    }

    @Before
    public void setUp() throws Exception {
        session.execute("truncate auctions");
        session.execute("truncate auction_bids");
        underTest = new AuctionDao(session);
        underTest.prepareStatements();
    }

    @Test
    public void createAndRetrieveDao() throws Exception {
        Auction expectedAuction = new Auction("ipad", "owner", Instant.now());

        underTest.createAuction(expectedAuction);
        Auction actualAuction = underTest.getAuction(expectedAuction.getName()).orElseThrow(() -> new AssertionError("Expected Auction not found"));

        assertEquals(expectedAuction.getName(), actualAuction.getName());
        assertEquals(expectedAuction.getOwner(), actualAuction.getOwner());
        assertEquals(expectedAuction.getEnds(), actualAuction.getEnds());
        assertEquals(expectedAuction.getBids(), actualAuction.getBids());
    }

    @Test
    public void getAllAuctions() throws Exception {
        Auction ipadAuction = new Auction("ipad", "owner1", Instant.now());
        underTest.createAuction(ipadAuction);
        Auction pcAuaction = new Auction("pc", "owner2", Instant.now());
        underTest.createAuction(pcAuaction);

        List<Auction> allAuctionsSparse = underTest.getAllAuctionsSparse();

        assertEquals(2, allAuctionsSparse.size());
        assertEquals("ipad", allAuctionsSparse.get(0).getName());
        assertEquals("owner1", allAuctionsSparse.get(0).getOwner());
        assertEquals("pc", allAuctionsSparse.get(1).getName());
        assertEquals("owner2", allAuctionsSparse.get(1).getOwner());
    }

    @Test
    public void addBid() throws Exception {
        Instant now = Instant.now();
        Auction expectedAuction = new Auction("ipad", "owner1", now);
        underTest.createAuction(expectedAuction);

        UUID uuid = underTest.placeBid(expectedAuction.getName(), "chbatey", 101l);
        Auction actualAuction = underTest.getAuction(expectedAuction.getName()).orElseThrow(() -> new AssertionError("Expected Auction not found"));

        assertEquals(expectedAuction.getName(), actualAuction.getName());
        assertEquals(expectedAuction.getEnds(), actualAuction.getEnds());
        assertEquals(expectedAuction.getOwner(), actualAuction.getOwner());
        assertEquals(Arrays.asList(new BidVo("chbatey", 101l, UUIDs.unixTimestamp(uuid))), actualAuction.getBids());
    }

    //todo: bid for a user that does not exist
}