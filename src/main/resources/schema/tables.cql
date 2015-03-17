CREATE KEYSPACE IF NOT EXISTS killrauction WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };
CREATE TABLE IF NOT EXISTS killrauction.users ( user_name text PRIMARY KEY, password text, salt bigint, first_name text, last_name text, emails set<text> ) ;
CREATE TABLE IF NOT EXISTS killrauction.auction_bids ( name text, bid_time timeuuid, bid_user text, bid_amount bigint, primary KEY (name, bid_amount, bid_time ) ) WITH CLUSTERING ORDER BY (bid_amount DESC )
CREATE TABLE IF NOT EXISTS killrauction.auctions ( name text primary key, ends bigint);
