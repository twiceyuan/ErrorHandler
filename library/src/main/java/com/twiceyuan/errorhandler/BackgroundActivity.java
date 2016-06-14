package com.twiceyuan.errorhandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by twiceYuan on 6/14/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 用于显示 Dialog 的背景 Activity
 */
public class BackgroundActivity extends Activity {

    private static DialogContextProvider mContextProvider;

    public static void show(DialogContextProvider contextProvider) {
        Context applicationContext = ErrorHandler.getInstance().getContext();
        mContextProvider = contextProvider;
        Intent intent = new Intent(applicationContext, BackgroundActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContextProvider.getActivity(this);
    }

    public interface DialogContextProvider {
        void getActivity(Activity activity);
    }
}
