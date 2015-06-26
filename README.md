Work in progress.

An example of how to use Cassandra to build an e-bay like Auction service. Uses the following tech stack:

![TechStack](https://raw.githubusercontent.com/chbatey/killrauction/master/techstack.png)

Initial Setup:
1. Get access to a Cassandra cluster ( local installation is more convenient, but you can use a cloud installation
 If using the cloud, obtain the PRIVATE ip of a node from the cluster.

2. Use files from src/main/resources/schema to create keyspace & tables in Cassandra. The DDL statements
only need to be physically run on a single node; Cassandra will take place of replicating schema to other nodes.

3. Build project in gradle ( ./gradlew clean build )

4. Start Web server ( src/main/java/Application.main)

5. Run tests from Gradle ( ./gradlew test, e2eTest, runLoadTest )


Regular Usage:
1. Start Cassandra

2. Start web server

3. Run tests from Gradle
