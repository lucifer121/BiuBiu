package com.android.biubiu.myapplication;

import org.xutils.BuildConfig;
import org.xutils.x;

import com.hyphenate.easeui.controller.EaseUI;

import android.app.Application;

public class BiubiuApplication extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
     //   EaseUI.getInstance().init(context, options);
	}
}
