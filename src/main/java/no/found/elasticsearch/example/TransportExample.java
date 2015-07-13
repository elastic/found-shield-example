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
        //TransportCommandLineParameters parameters = new TransportCommandLineParameters().parse(args, logger);

        //logger.info("Connecting to cluster: [{}] using api key: [{}] in region [{}]", parameters.clusterId, parameters.apiKey, //parameters.region);

        // Build the settings for our client.
        Settings settings = ImmutableSettings.settingsBuilder()
            // Setting "transport.type" enables this module:
            //.put("cluster.name", parameters.clusterId)
            //.put("client.transport.ignore_cluster_name", false)
            //.put("request.headers.foo", "this-is-my-bar")
            .put("transport.ping_schedule", "5s")
            //.put("transport.sniff", false)
            .put("cluster.name", "c7e20854fc104683ba6a44d3f20a73b9")

            //.put("request.headers.clusterName", "c7e20854fc104683ba6a44d3f20a73b9")
            //.put("request.headers.cluster", "c7e20854fc104683ba6a44d3f20a73b9")
            .put("request.headers.cluster_name", "clusterName")
            //.put("request.headers.found_cluster", "c7e20854fc104683ba6a44d3f20a73b9")
            //.put("request.headers.x_found_cluster", "c7e20854fc104683ba6a44d3f20a73b9")
            //.put("request.headers.X-Found-Cluster", "c7e20854fc104683ba6a44d3f20a73b9")
            //.put("request.headers.found_cluster_name", "c7e20854fc104683ba6a44d3f20a73b9")

            //.put("request.headers.apiKey", "omgfoo")
            //.put("shield.user", "test_user:changeme")


            .build();

        // Instantiate a TransportClient and add Found Elasticsearch to the list of addresses to connect to.
        // Only port 9343 (SSL-encrypted) is currently supported.
        //logger.info("Using url: [{}]", parameters.clusterId + "." + parameters.region + ".aws.found.io");

        Client client = new TransportClient(settings)
            //.addTransportAddress(new InetSocketTransportAddress(parameters.clusterId + "-" + parameters.region + ".foundcluster.com", 9343));
            .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
            //.addTransportAddress(new InetSocketTransportAddress("192.168.40.10", 19765));

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
