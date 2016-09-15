# Elastic Cloud Shield Transport Example

**For Elasticsearch < 2.0, see the https://github.com/elastic/found-shield-example/tree/1.x branch**

To run the example use ``mvn exec:java``:

    $ mvn compile exec:java -Dhost=YOUR_CLUSTER_ID.REGION.aws.found.io -Dshield.user="username:password"

Replace `YOUR_CLUSTER_ID` with your cluster id and `REGION` with the region the cluster is started in.

_(Note: by default, if IPv4 and IPv6 are both enabled then it is undetermined which will be used. The system properties `-Dipv4=true|false` and `-Dipv6=true|false` can be used to turn them on/off explicitly. If they are enabled then the network/host should support routing to the Cloud endpoint, eg this is not the case within Docker containers by default)._
