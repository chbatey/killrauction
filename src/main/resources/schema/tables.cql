CREATE KEYSPACE IF NOT EXISTS killrauction WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };

CREATE TABLE IF NOT EXISTS users ( user_name text PRIMARY KEY,  password text, first_name text, last_name text, emails set<text> ) ;