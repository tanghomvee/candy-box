package com.candybox.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static String postForm(String url) throws Exception {
        return postForm(url , null);
    }
    public static String postForm(String url , Map<String, ? extends Serializable> data) throws Exception {
        return postForm(url , data ,Consts.UTF_8);
    }
    public static String postForm(String url , Map<String, ? extends Serializable> data , String encoding) throws Exception {
        return postForm(url , data , Charset.forName(encoding));
    }
    public static String postForm(String url , Map<String, ? extends Serializable> data , Charset encoding) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        String body = "";
        //装填参数
        List<NameValuePair> nvps = Lists.newArrayList();
        if(data != null){
            for (Map.Entry<String, ? extends Serializable> entry : data.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
            }
        }
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        try{
           body = request(httpPost , encoding);
        }catch (Exception ex){
            LOGGER.error("请求url={},参数={}异常" , url , nvps.toString() , ex);
        }

        return body;
    }
    public static String postJSON(String url ) throws Exception {
        return postJSON(url , null);
    }
    public static String postJSON(String url , Object data) throws Exception {
        return postJSON(url , data ,Consts.UTF_8);
    }
    public static String postJSON(String url , Object data , String encoding) throws Exception {
        return postJSON(url , data ,Charset.forName(encoding));
    }
    public static String postJSON(String url , Object data , Charset encoding) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        String body = "" , content = data == null ? "" : JSONObject.toJSONString(data);
        //设置参数到请求对象中
        if (data != null ){
            httpPost.setEntity(new StringEntity(content, encoding));
        }
        //设置header信息
        httpPost.setHeader("Content-type", "application/json");
        try{
           body = request(httpPost , encoding);
        }catch (Exception ex){
            LOGGER.error("请求url={},参数={}异常" , url , content , ex);
        }

        return body;
    }


    public static InputStream downloadByJSON(String url , Object data) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        String  content = data == null ? "" : JSONObject.toJSONString(data);
        InputStream body = null;
        //设置参数到请求对象中
        if (data != null ){
            httpPost.setEntity(new StringEntity(content, Consts.UTF_8));
        }
        //设置header信息
        httpPost.setHeader("Content-type", "application/json");
        try{
            body = download(httpPost , Consts.UTF_8);
        }catch (Exception ex){
            LOGGER.error("请求url={},参数={}异常" , url , content , ex);
        }

        return body;
    }

    public static InputStream downloadByForm(String url , Map<String, ? extends Serializable> data) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        InputStream body = null;
        //装填参数
        List<NameValuePair> nvps = Lists.newArrayList();
        if(data != null){
            for (Map.Entry<String, ? extends Serializable> entry : data.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
            }
        }
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        try{
            body = download(httpPost , Consts.UTF_8);
        }catch (Exception ex){
            LOGGER.error("请求url={},参数={}异常" , url , nvps.toString() , ex);
        }

        return body;
    }


    private static  String request(HttpEntityEnclosingRequestBase requestBase , Charset encoding) throws IOException {
        String body = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try{
            httpResponse =  httpClient.execute(requestBase);
            HttpEntity respEntity = httpResponse.getEntity();
            if(respEntity != null){
                body = EntityUtils.toString(respEntity , encoding);
            }
            EntityUtils.consume(respEntity);
        }catch (Exception ex){
            throw ex;
        }finally {
            if(httpResponse != null){
                httpResponse.close();
            }
            if(httpClient != null){
                httpClient.close();
            }
        }

        return body;
    }
    private static  InputStream download(HttpEntityEnclosingRequestBase requestBase , Charset encoding) throws IOException {
        InputStream body = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try{
            httpResponse =  httpClient.execute(requestBase);
            HttpEntity respEntity = httpResponse.getEntity();
            if(respEntity != null){
                byte[] data = EntityUtils.toByteArray(respEntity);
                body = new ByteArrayInputStream(data);
            }
            EntityUtils.consume(respEntity);
        }catch (Exception ex){
            throw ex;
        }finally {
            if(httpResponse != null){
                httpResponse.close();
            }
            if(httpClient != null){
                httpClient.close();
            }
        }

        return body;
    }


    public static String postXML(String url, String data , Charset encoding) {
        HttpPost httpPost = new HttpPost(url);
        String body = "" , content = data ;
        //设置参数到请求对象中
        if (data != null ){
            httpPost.setEntity(new StringEntity(content, encoding));
        }
        //设置header信息
        httpPost.setHeader("Content-type", "text/xml");
        try{
            body = request(httpPost , encoding);
        }catch (Exception ex){
            LOGGER.error("请求url={},参数={}异常" , url , content , ex);
        }

        return body;
    }
}
