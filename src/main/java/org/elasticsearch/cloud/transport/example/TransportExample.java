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

package org.elasticsearch.cloud.transport.example;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

public class TransportExample {

    public Logger logger = ESLoggerFactory.getLogger(getClass().getCanonicalName());
    // Note: If enabling IPv6, then you should ensure that your host and network can route it to the Cloud endpoint.
    // (eg Docker disables IPv6 routing by default) - see also the system property parsing code below.
    private boolean ip6Enabled = true;
    private boolean ip4Enabled = true;

    public static void main(String[] args)  {
        new TransportExample().run(args);
    }

    public void run(String[] args) {
        String host = System.getProperty("host");
        int port = Integer.parseInt(System.getProperty("port", "9243"));

        String hostBasedClusterName = host.split("\\.", 2)[0];
        String clusterName = System.getProperty("cluster", hostBasedClusterName);

        boolean enableSsl = Boolean.parseBoolean(System.getProperty("ssl", "true"));
        // Note: If enabling IPv6, then you should ensure that your host and network can route it to the Cloud endpoint.
        // (eg Docker disables IPv6 routing by default) - see also the initialization code at the top of this file.
        ip6Enabled = Boolean.parseBoolean(System.getProperty("ip6", "true"));
        ip4Enabled = Boolean.parseBoolean(System.getProperty("ip4", "true"));

        logger.info("Connecting to cluster: [{}] via [{}:{}] using ssl:[{}]", clusterName, host, port, enableSsl);

        // Build the settings for our client.
        Settings settings = Settings.builder()
            .put("client.transport.nodes_sampler_interval", "5s")
            .put("client.transport.sniff", false)
            .put("transport.tcp.compress", true)
            .put("cluster.name", clusterName)
            .put("xpack.security.transport.ssl.enabled", enableSsl)
            .put("request.headers.X-Found-Cluster", "${cluster.name}")
            .put("xpack.security.user", System.getProperty("xpack.security.user"))
            .build();

        // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
        // Only port 9343 (SSL-encrypted) is currently supported. The use of X-Pack security features (formerly Shield) is required.
        TransportClient client = new PreBuiltXPackTransportClient(settings);
        try {
            for (InetAddress address : InetAddress.getAllByName(host)) {
                if ((ip6Enabled && address instanceof Inet6Address)
                        || (ip4Enabled && address instanceof Inet4Address)) {
                    client.addTransportAddress(new InetSocketTransportAddress(address, port));
                }
            }
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
