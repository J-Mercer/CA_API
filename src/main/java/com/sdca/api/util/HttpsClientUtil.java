package com.sdca.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

public class HttpsClientUtil {
    public static String  Post(String urlString, String param){
        String result = null;
        System.out.println(param);
        HttpPost httpPost = new HttpPost(urlString);
        StringEntity postEntity = new StringEntity(param,
                ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
        // 设置一些Http头信息
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("connection", "Keep-Alive");
        httpPost.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 6.1;SV1)");
        // 将发送内容填装
        httpPost.setEntity(postEntity);

        // 设置请求器的配置
        // httpPost.setConfig();
        HttpResponse response = null;

        HttpClient httpClient;
        try {
            httpClient = new SSLClient();
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");
            // 打印得到的响应信息
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
