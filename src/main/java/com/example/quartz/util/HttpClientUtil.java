package com.example.quartz.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Httpclient请求工具类
 *
 * @author hellofly
 * @date 2019/4/9
 */
public class HttpClientUtil {

    /**
     * 连接主机超时（30s）
     */
    public static final int HTTP_CONNECT_TIMEOUT_30S = 30 * 1000;

    /**
     * 从主机读取数据超时（3min）
     */
    public static final int HTTP_READ_TIMEOUT_3MIN = 180 * 1000;

    /**
     * HTTP成功状态码（200）
     */
    public static final int HTTP_SUCCESS_STATUS_CODE = 200;

    private static final Logger logger = LogManager.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
    }

    /**
     * get请求
     *
     * @param url
     * @param formDataParam
     * @return
     */
    public static String getMap(String url, Map<String, Object> formDataParam) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        // 超时时间设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(HTTP_READ_TIMEOUT_3MIN)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT_30S).build();

        try {
            URIBuilder builder = new URIBuilder(url);
            if (null != formDataParam && formDataParam.size() > 0) {
                // 创建参数队列
                List<NameValuePair> formParams = new ArrayList<>();
                for (Entry<String, Object> entry : formDataParam.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                builder.setParameters(formParams);
            }
            // 设置参数
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setConfig(requestConfig);

            // 发送请求
            response = httpclient.execute(httpGet);
            result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            if (response.getStatusLine().getStatusCode() != HTTP_SUCCESS_STATUS_CODE) {
                logger.error("Error in getMap. Request URL is [{}], params [{}]. Result:[{}]", url, formDataParam, result);
            }
        } catch (Exception e) {
            logger.error("Error in getMap", e);
        } finally {
            HttpClientUtils.closeQuietly(httpclient);
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }

    /**
     * postFormData
     *
     * @param url
     * @param formDataParam
     * @return
     */
    public static String postFormData(String url, Map<String, Object> formDataParam) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        // 超时时间设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(HTTP_READ_TIMEOUT_3MIN)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT_30S).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            if (null != formDataParam && formDataParam.size() > 0) {
                // 创建参数队列
                List<NameValuePair> formParams = new ArrayList<>();
                for (Entry<String, Object> entry : formDataParam.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                // 设置参数
                UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(formParams, Charsets.UTF_8);
                httpPost.setEntity(urlEntity);
            }

            // 发送请求
            response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            if (response.getStatusLine().getStatusCode() != HTTP_SUCCESS_STATUS_CODE) {
                logger.error("Error in postFormData. Request URL is [{}], params [{}]. Result:[{}]", url, formDataParam, result);
            }
        } catch (Exception e) {
            logger.error("Error in postFormData", e);
        } finally {
            HttpClientUtils.closeQuietly(httpclient);
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }

    /**
     * postJson
     *
     * @param url
     * @param jsonParam
     * @return
     */
    public static String postJson(String url, String jsonParam) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        // 超时时间设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(HTTP_READ_TIMEOUT_3MIN)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT_30S).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        // 设置请求头和请求参数
        if (StringUtils.isNotEmpty(jsonParam)) {
            StringEntity entity = new StringEntity(jsonParam, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }

        try {
            // 发送请求
            response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            if (response.getStatusLine().getStatusCode() != HTTP_SUCCESS_STATUS_CODE) {
                logger.error("Error in postJson. Request URL is [{}], params [{}]. Result:[{}]", url, jsonParam, result);
            }
        } catch (Exception e) {
            logger.error("Error in postJson", e);
        } finally {
            HttpClientUtils.closeQuietly(httpclient);
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }

}
