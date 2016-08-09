# Elastic Cloud Shield Transport Example

**For Elasticsearch < 2.0, see the https://github.com/elastic/found-shield-example/tree/1.x branch**

To run the example use ``mvn exec:java``:

    $ mvn compile exec:java -Dhost=YOUR_CLUSTER_ID.REGION.aws.found.io -Dshield.user="username:password"

Replace `YOUR_CLUSTER_ID` with your cluster id and `REGION` with the region the cluster is started in.
