package net.diskroom.weather;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/1.
 */
public class HttpRequestGetWeather {
    private static final String url="http://www.webxml.com.cn/WebServices/weatherWebService.asmx";

    public static ArrayList<String> getWeatherByCityName(String cityName){
        Map<String,String> params = new HashMap<String,String>();
        params.put("theCityName",cityName);
        String xml = HttpUtils.post(url+"/getWeatherbyCityName",params,"utf-8");
        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(xml.getBytes());
        ArrayList<String> weather = null;
        try {
            weather = parseWeatherXml.parse(tInputStringStream);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return weather;
    }



}
