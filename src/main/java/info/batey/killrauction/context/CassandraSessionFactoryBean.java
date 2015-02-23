package info.batey.killrauction.context;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class CassandraSessionFactoryBean implements FactoryBean<Session> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraSessionFactoryBean.class);
    private Cluster cluster;

    @Override
    public Session getObject() throws Exception {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        return cluster.connect("killrauction");
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
