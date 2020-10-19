package com.hochoy.elasticsearch;

import com.hochoy.utils.HochoyUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import scala.util.parsing.json.JSON;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Date;
import java.util.Properties;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class TEST {
    public static  RestHighLevelClient getRestHighLevelClient() {

        Properties properties = HochoyUtils.getProperties("es.properties");
        String host = properties.getProperty("es.host");
        int port = Integer.parseInt(properties.getProperty("es.port"));

        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "http")));
    }

    public static void main(String[] args) {

    }

}
