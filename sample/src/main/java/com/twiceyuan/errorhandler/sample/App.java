package com.twiceyuan.errorhandler.sample;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.twiceyuan.errorhandler.DebugDialog;
import com.twiceyuan.errorhandler.ErrorHandler;
import com.twiceyuan.errorhandler.ExceptionListener;
import com.twiceyuan.errorhandler.sample.exceptions.FirstException;
import com.twiceyuan.errorhandler.sample.exceptions.SecondException;

/**
 * Created by twiceYuan on 6/12/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class App extends Application {

    private static Context sInstance;

    public static Context getInstance() {
        return sInstance;
    }

    @Override public void onCreate() {
        super.onCreate();
        sInstance = this;

        // 初始化，初始化后表示正式启用
        ErrorHandler.init(this);

        // 非主线程的异常，可以直接进行处理而不打断程序运行
        ErrorHandler.addHandler(FirstException.class, new ExceptionListener<FirstException>() {
            @Override public void handle(FirstException e) {
                Toast.makeText(getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ErrorHandler.addHandler(SecondException.class, new ExceptionListener<SecondException>() {
            @Override public void handle(SecondException e) {
                if (BuildConfig.DEBUG) {
                    DebugDialog.show(e);
                }
            }
        });
    }
}
