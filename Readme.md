# Elastic Cloud X-Pack Transport Example

**Note:** This branch is intended for use with Elasticsearch 6.x, see the below table for earlier versions.

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
