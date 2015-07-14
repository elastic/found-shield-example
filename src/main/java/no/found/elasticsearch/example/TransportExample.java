package no.found.elasticsearch.example;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.util.concurrent.TimeUnit;

public class TransportExample {

    public ESLogger logger = ESLoggerFactory.getLogger(getClass().getCanonicalName());

    public static void main(String[] args)  {
        new TransportExample().run(args);
    }

    public void run(String[] args) {
        String host = System.getProperty("host");
        int port = Integer.parseInt(System.getProperty("port", "9343"));

        String hostBasedClusterName = host.split("\\.", 2)[0];
        String clusterName = System.getProperty("cluster", hostBasedClusterName);

        logger.info("Connecting to cluster: [{}] via [{}:{}]", clusterName, host, port);

        // Build the settings for our client.
        Settings settings = ImmutableSettings.settingsBuilder()
            .put("transport.ping_schedule", "5s")
            //.put("transport.sniff", false)
            .put("cluster.name", clusterName)
            .put("request.headers.X-Found-Cluster", "${cluster.name}")
                .build();

        // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
        // Only port 9343 (SSL-encrypted) is currently supported.
        Client client = new TransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress(host, port));

        while(true) {
            try {
                logger.info("Getting cluster health... ");
                ActionFuture<ClusterHealthResponse> healthFuture = client.admin().cluster().health(Requests.clusterHealthRequest());
                ClusterHealthResponse healthResponse = healthFuture.get(5, TimeUnit.SECONDS);
                logger.info("Got cluster health response: [{}]", healthResponse.getStatus());
            } catch(Throwable t) {
                logger.error("Unable to get cluster health response: [{}]", t.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) { ie.printStackTrace(); }
        }
    }
}
