/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
 
package no.found.elasticsearch.example;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

        boolean enableSsl = Boolean.parseBoolean(System.getProperty("ssl", "true"));

        logger.info("Connecting to cluster: [{}] via [{}:{}] using ssl:[{}]", clusterName, host, port, enableSsl);

        // Build the settings for our client.
        Settings settings = Settings.settingsBuilder()
            .put("transport.ping_schedule", "5s")
            //.put("transport.sniff", false)
            .put("cluster.name", clusterName)
            .put("action.bulk.compress", false)
            .put("shield.transport.ssl", enableSsl)
            .put("request.headers.X-Found-Cluster", "${cluster.name}")
            .put("shield.user", System.getProperty("shield.user"))
            .build();

        // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
        // Only port 9343 (SSL-encrypted) is currently supported.
        Client client = null;
        try {
            client = TransportClient.builder()
                    .addPlugin(ShieldPlugin.class).settings(settings)
                    .settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            logger.error("Unable to get the host", e.getMessage());
        }

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
