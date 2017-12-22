package com.example.wpx.framework.webservice;

import com.alibaba.fastjson.JSON;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h3>Description</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/11/6 20:02
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class WebServiceManager {

    private static final String WSDL_URI = "http://121.37.17.177:50003/IHBase";//WDSL的URL

    private static final String NAMESPACE = "http://impl.webservice/";//命名空间

    private static ExecutorService services = Executors.newFixedThreadPool(5);

    private WebServiceManager() {

    }

    private static WebServiceManager instance;

    public static WebServiceManager getInstance() {
        if (instance == null) {
            synchronized (WebServiceManager.class) {
                if (instance == null) {
                    instance = new WebServiceManager();
                }
            }
        }
        return instance;
    }

    public static <T> void call(final String methodName, final Map<String, String> pargrams, final Class<T> tClass, final WebServiceListener<T> listener) {
        services.execute(new Runnable() {
            @Override
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, methodName);
                //添加参数
                for (Map.Entry<String, String> enrty : pargrams.entrySet()) {
                    request.addProperty(enrty.getKey(), enrty.getValue());
                }
                //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER10);
                envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
                envelope.dotNet = false;//由于是.net开发的webservice，所以这里要设置为true
                HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
                try {
                    httpTransportSE.call(null, envelope);//调用
                } catch (IOException e) {
                    listener.onIOException(e);
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    listener.onXmlPullParserException(e);
                    e.printStackTrace();
                }
                // 获取返回的数据
                SoapObject object = (SoapObject) envelope.bodyIn;
                // 获取返回的结果
                if (object != null) {
                    String resultStr = object.getProperty(0).toString();
                    T t = JSON.parseObject(resultStr, tClass);
                    listener.onSuccess(t);
                }
            }
        });
    }

}
