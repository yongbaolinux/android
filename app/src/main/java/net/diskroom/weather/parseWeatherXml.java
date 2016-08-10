package net.diskroom.weather;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

/**
 * Created by Administrator on 2016/7/3.
 */
public class parseWeatherXml{

    public static ArrayList<String> parse(InputStream is) throws Exception {
        ArrayList<String> mList = null;

        // 由android.util.Xml创建一个XmlPullParser实例
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is,"UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    mList = new ArrayList<String>(); // 初始化 weather信息
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("string")) { // 判断开始标签元素是否是 string
                        eventType = xpp.next();
                        mList.add(xpp.getText());
                    }
                    break;

            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        return mList;

    }


}
