package com.qmetric.spark.metrics;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;

import java.io.IOException;

import static com.qmetric.spark.metrics.SparkConstants.PORT;

class SparkTestUtil
{

    private final DefaultHttpClient httpClient;

    private static final String URL_TEMPLATE = "http://localhost:%d/%s";

    public SparkTestUtil(final int port)
    {
        Scheme http = new Scheme("http", port, PlainSocketFactory.getSocketFactory());
        SchemeRegistry sr = new SchemeRegistry();
        sr.register(http);
        ClientConnectionManager connMrg = new BasicClientConnectionManager(sr);
        httpClient = new DefaultHttpClient(connMrg);
    }

    public HttpResponse get(final String s)
    {
        final String cleaned = s.replace("/", "");
        try
        {
            return httpClient.execute(new HttpGet(String.format(URL_TEMPLATE, PORT, cleaned)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
