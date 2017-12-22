package com.example.wpx.framework.webservice;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <h3>Description</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/11/6 20:02
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public interface WebServiceListener<T>{

    void onSuccess(T t);

    void onIOException(IOException e);

    void onXmlPullParserException(XmlPullParserException e);

}
