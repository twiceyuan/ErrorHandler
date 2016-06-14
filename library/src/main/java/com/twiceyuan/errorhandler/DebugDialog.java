package com.twiceyuan.errorhandler;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by twiceYuan on 6/14/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 用于 Debug 的 Dialog
 */
public class DebugDialog {

    public static void show(final Activity context, final Throwable error) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog);
        final ScrollView container = new ScrollView(context);
        final TextView textErrorInfo = new TextView(context);
        final TextView textMessage = new TextView(context);

        textErrorInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textErrorInfo.setBackgroundColor(0xFF2B2B2B);

        textErrorInfo.setTextColor(0xFFFF6B68);

        int dp4 = Utils.dp(4);
        int dp16 = Utils.dp(16);

        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textMessage.setTextColor(0xffffffff);
        textMessage.setPadding(dp16, dp16, dp16, dp16);
        textMessage.setGravity(Gravity.CENTER);
        textMessage.setText("程序抛出了一个异常");

        textErrorInfo.setPadding(dp16, dp16, dp16, dp16);
        textErrorInfo.setText(Log.getStackTraceString(error));

        container.addView(textMessage);
        container.setPadding(dp4, dp4, dp4, dp4);

        builder.setView(container);
        builder.setNeutralButton("查看错误信息", null);
        builder.setPositiveButton("好的", null);
        builder.setNegativeButton("打印日志", null);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                context.finish();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final Button errorInfoButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        final Button printInfoButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        errorInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (errorInfoButton.getText().toString().equals("查看错误信息")) {
                    container.removeAllViews();
                    container.addView(textErrorInfo);
                    errorInfoButton.setText("隐藏错误信息");
                    printInfoButton.setText("转发日志");
                } else {
                    container.removeAllViews();
                    container.addView(textMessage);
                    errorInfoButton.setText("查看错误信息");
                    printInfoButton.setText("打印日志");
                }
            }
        });

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                context.finish();
            }
        });

        printInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (printInfoButton.getText().toString().equals("转发日志")) {
                    Utils.share(Log.getStackTraceString(error));
                }
                if (printInfoButton.getText().toString().equals("打印日志")) {
                    Log.e("ReportActivity", "手动打印日志", error);
                    Toast.makeText(context, "日志已打印", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void show(final Throwable error) {
        BackgroundActivity.show(new BackgroundActivity.DialogContextProvider() {
            @Override public void getActivity(Activity activity) {
                show(activity, error);
            }
        });
    }
}
