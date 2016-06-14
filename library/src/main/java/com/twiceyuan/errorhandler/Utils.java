package com.twiceyuan.errorhandler;

import android.content.Intent;

/**
 * Created by twiceYuan on 6/14/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 工具类
 */
public class Utils {

    public static int dp(int dp) {
        return (int) (ErrorHandler.getInstance().getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static void share(String shareContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ErrorHandler.getInstance().getContext().startActivity(intent);
    }
}
