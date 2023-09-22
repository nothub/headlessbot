package lol.hub.headlessbot;

import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;

public class Metrics {

    public static final Counter deaths = Counter.build()
        .name("deaths")
        .help("Total deaths.")
        .register();

    static void init(final HttpServer server) throws IOException {
        // hotspot metrics
        DefaultExports.initialize();

        // register exporter
        new HTTPServer.Builder().withHttpServer(server).build();
    }
}
