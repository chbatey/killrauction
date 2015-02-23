package info.batey.killrauction.context;

import com.codahale.metrics.MetricRegistry;
import com.datastax.driver.core.Session;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SpringConfiguringClass extends MetricsConfigurerAdapter {

    @Inject
    private Session session;

    @Override
    public MetricRegistry getMetricRegistry() {
        return session.getCluster().getMetrics().getRegistry();
    }
}
