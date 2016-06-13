package com.twiceyuan.errorhandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

/**
 * Created by twiceYuan on 6/12/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 错误拦截器
 */
public class ErrorHandler implements Thread.UncaughtExceptionHandler {

    public static final String ERROR_CAUSE = "ErrorHandler_ErrorCause";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 接收主线程错误处理的 Activity
     */
    private Class<? extends Activity> mMainHandler = ReportActivity.class; // 默认的 Activity Handler

    private Context mContext;

    /**
     * 异常 handler map，存储从 throwable -> exception handler 的映射
     */
    private ExceptionMap mExceptionMap;

    public static ErrorHandler getInstance() {
        return Singleton.sInstance;
    }

    private ErrorHandler() {
        // 保存原有的 ErrorHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        mExceptionMap = new ExceptionMap<>();
    }

    private static class Singleton {
        private static final ErrorHandler sInstance = new ErrorHandler();
    }

    public static void init(Context context) {
        Singleton.sInstance.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(Singleton.sInstance);
    }

    @Override public void uncaughtException(Thread thread, final Throwable ex) {

        if (thread == Looper.getMainLooper().getThread() && mMainHandler != null) {
            // 新建一个 Task，跳转到接收主线程错误的 Activity，并结束当前线程
            Intent intent = new Intent(mContext, mMainHandler);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ERROR_CAUSE, ex);
            mContext.startActivity(intent);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleException(Throwable throwable) {
        ExceptionListener listener = (ExceptionListener) mExceptionMap.get(throwable.getClass());
        if (listener != null) {
            Looper.prepare();
            try {
                //noinspection unchecked
                listener.handle(throwable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Looper.loop();
            return true;
        }
        return false;
    }

    public static <T extends Throwable> void addHandler(Class<T> tClass, ExceptionListener<T> listener) {
        Singleton.sInstance.addHandlerMethod(tClass, listener);
    }

    /**
     * 设置接收主线程 Exception 的 Activity
     * @param activityClass 接收主线程的 Activity 的 class
     */
    public static void setMainThreadHandler(Class<Activity> activityClass) {
        Singleton.sInstance.mMainHandler = activityClass;
    }

    private <T extends Throwable> void addHandlerMethod(Class<T> tClass, ExceptionListener<T> listener) {
        if (mExceptionMap.keySet().contains(tClass)) {
            Log.i("ErrorHandler", "Handler of " + tClass.getName() + " will be replaced");
        }
        //noinspection unchecked
        mExceptionMap.put(tClass, listener);
    }
}
