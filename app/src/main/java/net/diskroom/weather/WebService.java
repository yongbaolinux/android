package net.diskroom.weather;

import com.apkfuns.logutils.LogUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Administrator on 2016/7/1.
 */

public class WebService {
    private static final String nameSpace = "http://WebXml.com.cn/";
    private static final String asmx = "http://www.webxml.com.cn/WebServices/weatherWebService.asmx";

    //根据城市名获取该市的天气
    public static SoapObject getWeatherByCityName(String cityName) {
        String methodName = "getWeatherbyCityName";

        //初始化 rpc
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        // 设置需调用WebService接口需要传入的 cityName 参数
        rpc.addProperty("theCityName", cityName);

        //初始化 envelope (生成调用WebService方法的SOAP请求信息,并指定SOAP的版本)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;

        //请求调用 webService
        HttpTransportSE transport = new HttpTransportSE(asmx);
        transport.debug = false;

        try {
            transport.call("http://WebXml.com.cn/getWeatherbyCityName", envelope);
            LogUtils.tag("weather_").v("abc");
        } catch (Exception e) {
            e.printStackTrace();

        }

        //处理返回的结果
        SoapObject object = null;
        try {
            object = (SoapObject) envelope.getResponse();
        } catch(Exception e){
            e.printStackTrace();
        }

        // 获取返回的结果
        //String result = object.getProperty(0).toString();
        return object;
    }
}
