package net.diskroom.weather;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.net.URL;
import java.io.IOException;

/**
 * Created by Administrator on 2016/7/1.
 */
public class HttpUtils {
    /**
     * POST请求
     * @param url       请求url地址
     * @param postData  post数据
     * @param strEncode post数据编码 例如:utf-8
     * @return          服务器响应
     */
    public static String post(String url,Map<String,String> postData,String strEncode){
        byte[] data = getRequestData(postData,strEncode).toString().getBytes();
        try {
            URL url_ = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection)url_.openConnection();
            urlConn.setConnectTimeout(3000);                            //设置连接超时时间
            urlConn.setDoOutput(true);                                  //打开输出流 以便向服务器提交数据
            urlConn.setDoInput(true);                                   //打开输入流 以便从服务器获取数据
            urlConn.setRequestMethod("POST");                           //设置请求方式
            urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //设置请求头属性
            urlConn.setRequestProperty("Content-length", data.length + "");
            OutputStream opstream = urlConn.getOutputStream();          //打开输出流
            opstream.write(data);                                       //写入数据到输出流

            if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream ipstream = urlConn.getInputStream();        //读取输入流
                return dealResponseResult(ipstream);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "-1";
    }

    /**
     *
     */
    public static String get(String url){
        try {
            URL url_ = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection)url_.openConnection();
            urlConn.setConnectTimeout(3000);              //设置连接超时时间
            urlConn.setDoInput(true);                     //打开输入流 以便获取服务器的响应信息
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //设置请求头属性
            if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream ipstream = urlConn.getInputStream();        //读取输入流
                return dealResponseResult(ipstream);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "-1";                                      //若请求发生异常 返回 "-1"
    }

    /**
     * 封装请求体信息
     *
     */
    private static StringBuffer getRequestData(Map<String,String> postData,String strEncode){
        StringBuffer strBuff = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : postData.entrySet()) {
                strBuff.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), strEncode)).append('&');
            }
            strBuff.substring(0, -1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return strBuff;
    }

    /*
    * Function  :   处理服务器的响应结果（将输入流转化成字符串）
    * Param     :   inputStream服务器的响应输入流
    */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
}
