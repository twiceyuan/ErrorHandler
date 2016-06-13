package com.twiceyuan.errorhandler;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

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

        final Throwable error = (Throwable) getIntent().getSerializableExtra(ErrorHandler.ERROR_CAUSE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ScrollView container = new ScrollView(this);
        final TextView textErrorInfo = new TextView(this);
        final TextView textMessage = new TextView(this);

        textErrorInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textErrorInfo.setBackgroundColor(0xFF2B2B2B);

        textErrorInfo.setTextColor(0xFFFF6B68);

        int dp4 = dp(4);
        int dp16 = dp(16);

        textMessage.setPadding(dp16, dp16, dp16, dp16);
        textMessage.setGravity(Gravity.CENTER);
        textMessage.setText("错误信息已收集");

        textErrorInfo.setPadding(dp16, dp16, dp16, dp16);
        textErrorInfo.setText(stackTrace(error));

        container.addView(textMessage);
        container.setPadding(dp4, dp4, dp4, dp4);

        builder.setView(container);
        builder.setNeutralButton("查看错误信息", null);
        builder.setPositiveButton("好的", null);
        builder.setNegativeButton("打印日志", null);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final Button button = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (button.getText().toString().equals("查看错误信息")) {
                    container.removeAllViews();
                    container.addView(textErrorInfo);
                    button.setText("隐藏错误信息");
                } else {
                    container.removeAllViews();
                    container.addView(textMessage);
                    button.setText("查看错误信息");
                }
            }
        });

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.e("ReportActivity", "手动打印日志", error);
                Toast.makeText(ReportActivity.this, "日志已打印", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String stackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        PrintWriter writer1 = new PrintWriter(writer);
        throwable.printStackTrace(writer1);
        return writer.toString();
    }

    private int dp(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
