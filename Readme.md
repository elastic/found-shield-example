# Elastic Cloud X-Pack Transport Example

**Note:** The Elasticsearch transport client is not supported at this point for accessing 6.0 Elasticsearch clusters managed by ECE. We are working on bringing back support for this, but we highly recommend for users to move to the [high level Java REST client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html).

| Elasticsearch version | Branch                                                                |
|-----------------------|-----------------------------------------------------------------------|
|                   6.x | [master](https://github.com/elastic/found-shield-example/tree/master) |
|                   5.x | [5.x](https://github.com/elastic/found-shield-example/tree/5.x)       |
|                   2.x | [2.x](https://github.com/elastic/found-shield-example/tree/2.x)       |
|                   1.x | [1.x](https://github.com/elastic/found-shield-example/tree/1.x)       |


## Running

To run the example use ``mvn exec:java``:

    $ mvn compile exec:java -Dhost=YOUR_CLUSTER_ID.REGION.aws.found.io -Dxpack.security.user='username:password'

Replace `YOUR_CLUSTER_ID` with your cluster id and `REGION` with the region the cluster is started in.


## IPv4 / IPv6

By default, if IPv4 and IPv6 are both enabled then it is undetermined which will be used. The system properties `-Dip4=true|false` and `-Dip6=true|false` can be used to turn them on/off explicitly. If they are enabled then the network/host should support routing to the Cloud endpoint, e.g. this is not the case within Docker containers by default).
