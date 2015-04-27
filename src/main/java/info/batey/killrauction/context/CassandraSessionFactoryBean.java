package info.batey.killrauction.context;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

@Component
public class CassandraSessionFactoryBean implements FactoryBean<Session> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraSessionFactoryBean.class);

    @Inject
    private CassandraConfiguration cassandraConfiguration;

    private Cluster cluster;

    @Override
    public Session getObject() throws Exception {
        LOGGER.info("Connecting to Cassandra {}", cassandraConfiguration);
        String[] hosts = new String[cassandraConfiguration.getContactPoints().size()];
        cluster = Cluster.builder()
                .addContactPoints(cassandraConfiguration.getContactPoints().toArray(hosts))
                .build();
        return cluster.connect(cassandraConfiguration.getKeyspace());
    }

    @Override
    public Class<?> getObjectType() {
        return Session.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Shutting down cluster");
        cluster.close();
    }
}
