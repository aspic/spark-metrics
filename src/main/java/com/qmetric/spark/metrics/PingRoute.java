package com.qmetric.spark.metrics;

import com.codahale.metrics.servlets.PingServlet;
import spark.Request;
import spark.Response;
import spark.Route;

public class PingRoute extends Route
{
    public static final String PATH = "/ping";

    final PingServlet pingServlet = new PingServlet();

    public PingRoute()
    {
        super(PATH);
    }

    @Override public Object handle(final Request request, final Response response)
    {
        try
        {
            pingServlet.service(request.raw(), response.raw());
        }
        catch (Exception e)
        {
            return (e.getMessage());
        }
        return null;
    }
}
