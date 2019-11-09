package neu.edu.csye6225.assignment2.helper;

import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
@Configuration
@EnableMetrics(proxyTargetClass=true)
public class MetricsConfig extends MetricsConfigurerAdapter {
    private static final Logger LOGGER= LogManager.getLogger();
    @Value("${management.metrics.export.statsd.prefix}")
    private String prefix;

    @Value("${management.metrics.export.statsd.host}")
    private String host;

    @Value("${management.metrics.export.statsd.port}")
    private int port;

    @Bean(name="statsDClient")
    public StatsDClient GetStatsDClient()
    {
        return new NonBlockingStatsDClient(prefix,host,port);
    }

}
