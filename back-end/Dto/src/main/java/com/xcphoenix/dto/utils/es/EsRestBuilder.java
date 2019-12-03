package com.xcphoenix.dto.utils.es;

import com.xcphoenix.dto.utils.UrlUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

/**
 * @author      xuanc
 * @date        2019/10/18 下午9:56
 * @version     1.0
 */ 
public class EsRestBuilder {

    private EsRestBuilder() {}

    /**
     * 设置 es RestClient
     */
    public static RestClient buildSearchRestClient() {
        org.elasticsearch.client.RestClientBuilder clientBuilder = RestClient.builder(
                new HttpHost("47.94.5.149", 9200, "http"));
        Header[] defaultHeaders = new Header[] {
                new BasicHeader("ContentType", "application/json")
        };
        clientBuilder.setDefaultHeaders(defaultHeaders);

        return clientBuilder.build();
    }

    public static Request setRestRequest(int size, int from) {
        return setRestRequest(size, from, "/restaurant/_search");
    }

    public static Request setRestRequest(int size, int from, String searchUrl) {
        size = Math.min(Math.max(0, size), 20);
        from = Math.max(0, from);
        UrlUtils getUrlUtils = new UrlUtils(searchUrl);
        getUrlUtils.setValue("size", size);
        getUrlUtils.setValue("from", from);

        return new Request(
                "GET",
                getUrlUtils.toString()
        );
    }

}
