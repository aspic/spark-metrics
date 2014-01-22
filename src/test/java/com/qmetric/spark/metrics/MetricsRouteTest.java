package com.qmetric.spark.metrics;

import com.codahale.metrics.MetricRegistry;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.StringWriter;

import static com.qmetric.spark.metrics.SparkConstants.PORT;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class MetricsRouteTest
{
    private SparkTestUtil sparkTestUtil;

    @Before
    public void init()
    {
        final MetricRegistry metricRegistry = new MetricRegistry();
        Spark.get(new RouteMeterWrapper(metricRegistry, new RouteTimerWrapper(PingRoute.PATH, metricRegistry, new PingRoute())));
        Spark.get(new MetricsRoute(metricRegistry));

        sparkTestUtil = new SparkTestUtil(PORT);

        final HttpResponse httpResponse = sparkTestUtil.get("ping");
        EntityUtils.consumeQuietly(httpResponse.getEntity());
    }

    @Test
    public void shouldShowMetrics() throws IOException
    {
        final HttpResponse httpResponse = sparkTestUtil.get("ping");
        EntityUtils.consumeQuietly(httpResponse.getEntity());
        final HttpResponse metrics = sparkTestUtil.get(MetricsRoute.PATH);

        final StringWriter writer = new StringWriter();
        IOUtils.copy(metrics.getEntity().getContent(), writer);

        assertThat(writer.toString(), containsString("timer.ping"));
        assertThat(writer.toString(), containsString("meter.ping"));
    }
}
