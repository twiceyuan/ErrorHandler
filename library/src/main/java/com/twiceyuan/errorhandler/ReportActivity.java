package com.twiceyuan.errorhandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

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

    public static final String EXTRA_ERROR = "error_info";

    public static void start(Context context, Throwable error) {
        Intent starter = new Intent(context, ReportActivity.class);
        starter.putExtra(EXTRA_ERROR, error);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        final Throwable error = (Throwable) getIntent().getSerializableExtra(EXTRA_ERROR);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ScrollView container = new ScrollView(this);
        final TextView textError = new TextView(this);
        textError.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textError.setBackgroundColor(0xFF2B2B2B);
        textError.setTextColor(0xFFFF6B68);
        container.addView(textError);

        container.setPadding(dp(4), dp(4), dp(4), dp(4));
        textError.setPadding(dp(16), dp(16), dp(16), dp(16));
        setText(textError, "错误信息已收集");
        builder.setView(container);
        builder.setNeutralButton("查看错误信息", null);
        builder.setPositiveButton("好的", null);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final Button button = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (button.getText().toString().equals("查看错误信息")) {
                    setText(textError, stackTrace(error));
                    button.setText("隐藏错误信息");
                } else {
                    setText(textError, "错误信息已收集");
                    button.setText("查看错误信息");
                }
            }
        });

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
    }

    private void setText(TextView textView, String s) {
        textView.setText(s);
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
