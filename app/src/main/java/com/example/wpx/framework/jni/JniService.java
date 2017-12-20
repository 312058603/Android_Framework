package com.example.wpx.framework.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wpx.framework.R;

public class JniService{
    public native String stringFromJNI();
    static {
        System.loadLibrary("native-lib");
    }
}
