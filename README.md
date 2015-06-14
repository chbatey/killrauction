Work in progress.

An example of how to use Cassandra to build an e-bay like Auction service. Uses the following tech stack:

![TechStack](https://raw.githubusercontent.com/chbatey/killrauction/master/techstack.png)

Setup Steps: 
1. Set up Cassandra ( local is more convenient, but you can use a cloud installation if you have one)

2. Use files from src/main/resources/schema to create keyspace & tables in the Cassandra cluster. The DML statements
only have to be physically run on a single node; Cassandra will take place of replicating schema to other nodes.

3. Build project in gradle ( ./gradlew clean build )

4. Start Web server ( src/main/java/Application.main)

5. Run tests from Gradle ( ./gradlew test, e2eTest, runLoadTest )

