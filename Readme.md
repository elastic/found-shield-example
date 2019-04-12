# Elastic Cloud X-Pack Transport Example

**Note:** The Elasticsearch transport client is officially [deprecated](https://www.elastic.co/guide/en/elasticsearch/client/java-api/master/transport-client.html) in `7.0.0` and we highly recommend for users to move to the [high level Java REST client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html).

| Elasticsearch version | Branch                                                                |
|-----------------------|-----------------------------------------------------------------------|
|                   7.x | [master](https://github.com/elastic/found-shield-example/tree/master) |
|                   6.x | [6.x](https://github.com/elastic/found-shield-example/tree/6.x)       |
|                   5.x | [5.x](https://github.com/elastic/found-shield-example/tree/5.x)       |
|                   2.x | [2.x](https://github.com/elastic/found-shield-example/tree/2.x)       |


## Running

To run the example use ``mvn exec:java``:

    $ mvn compile exec:java -Dhost=YOUR_CLUSTER_ID.REGION.aws.found.io -Dxpack.security.user='username:password'

Replace `YOUR_CLUSTER_ID` with your cluster id and `REGION` with the region the cluster is started in.


## Package

If you want to build an *uber-jar*, a package which contains the code and all of the dependencies, use the following command:

```sh
$ mvn compile assembly:single
```

The package will be build under `target/transport-example-VERSION-jar-with-dependencies.jar`.

Then you can run it:

```sh
$ java -Dhost=YOUR_CLUSTER_ID.REGION.aws.found.io -Dxpack.security.user='username:password' -jar target/transport-example-VERSION-jar-with-dependencies.jar
```

## IPv4 / IPv6

By default, if IPv4 and IPv6 are both enabled then it is undetermined which will be used. The system properties `-Dip4=true|false` and `-Dip6=true|false` can be used to turn them on/off explicitly. If they are enabled then the network/host should support routing to the Cloud endpoint, e.g. this is not the case within Docker containers by default).
