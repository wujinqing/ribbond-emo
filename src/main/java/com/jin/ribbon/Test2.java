package com.jin.ribbon;

import com.netflix.client.ClientFactory;
import com.netflix.client.IClient;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.niws.client.http.RestClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author wu.jinqing
 * @date 2019年12月21日
 */
public class Test2 {
    public static void main(String[] args) throws Exception{
        ConfigurationManager.loadPropertiesFromResources("sale-service.properties");

        RestClient client = (RestClient)ClientFactory.getNamedClient("sale-service");

        HttpRequest request = HttpRequest.newBuilder().uri("/getName").build();

        for(int i = 0; i < 10; i++) {
            HttpResponse response = client.executeWithLoadBalancer(request);

            InputStream is = response.getInputStream();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf, 0, 1024)) != -1) {
                buffer.write(buf, 0, len);
            }

            String s = new String(buffer.toByteArray(), "UTF-8");

            System.out.println(s);
        }
    }
}
