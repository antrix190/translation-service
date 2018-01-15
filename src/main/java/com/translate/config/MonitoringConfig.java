package com.translate.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import com.readytalk.metrics.StatsDReporter;

import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics
public class MonitoringConfig extends MetricsConfigurerAdapter {
    @Value("${statsD.host}")
    public String hostIP;
    @Value("${statsD.port}")
    public int    port;
    @Value("${statsD.period}")
    public int    period;
    @Value("${statsD.prefix}")
    public String prefix;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        StatsDReporter reporter = getStatsDReporterBuilder(metricRegistry).build(hostIP, port);

        registerReporter(reporter);
        reporter.start(period, TimeUnit.SECONDS);
    }

    private com.readytalk.metrics.StatsDReporter.Builder getStatsDReporterBuilder(MetricRegistry metricRegistry) {
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());

        return StatsDReporter.forRegistry(metricRegistry)
                             .prefixedWith(prefix)
                             .convertRatesTo(TimeUnit.SECONDS)
                             .convertDurationsTo(TimeUnit.MILLISECONDS)
                             .filter(MetricFilter.ALL);
    }
}
