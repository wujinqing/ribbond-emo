package com.jin.ribbon;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.client.http.HttpClientRequest;
import com.netflix.niws.client.http.RestClient;


import java.net.URI;

/**
 * @author wu.jinqing
 * @date 2019年12月21日
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        ConfigurationManager.loadPropertiesFromResources("sample-client.properties");  // 1
        System.out.println(ConfigurationManager.getConfigInstance().getProperty("sample-client.ribbon.listOfServers"));
        RestClient client = (RestClient) ClientFactory.getNamedClient("sample-client");  // 2
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("/")).build(); // 3
        for (int i = 0; i < 20; i++)  {
            HttpResponse response = client.executeWithLoadBalancer(request); // 4
            System.out.println("Status code for " + response.getRequestedURI() + "  :" + response.getStatus());
        }
        ZoneAwareLoadBalancer lb = (ZoneAwareLoadBalancer) client.getLoadBalancer();
        System.out.println(lb.getLoadBalancerStats());
        ConfigurationManager.getConfigInstance().setProperty(
                "sample-client.ribbon.listOfServers", "www.linkedin.com:80,www.google.com:80"); // 5
        System.out.println("changing servers ...");
        Thread.sleep(3000); // 6
        for (int i = 0; i < 20; i++)  {
            HttpResponse response = client.executeWithLoadBalancer(request);
            System.out.println("Status code for " + response.getRequestedURI() + "  : " + response.getStatus());
            response.close();
        }
        System.out.println(lb.getLoadBalancerStats()); // 7
    }
}
