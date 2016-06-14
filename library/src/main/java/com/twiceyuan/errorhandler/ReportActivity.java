package com.twiceyuan.errorhandler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by twiceYuan on 6/13/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 默认的报告界面
 */
public class ReportActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        DebugDialog.show(this, ErrorHandler.getThrowable(this));
    }
}
