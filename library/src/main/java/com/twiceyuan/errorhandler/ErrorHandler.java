package com.twiceyuan.errorhandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

/**
 * Created by twiceYuan on 6/12/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p/>
 * 错误拦截器
 */
public class ErrorHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context                         mContext;

    /**
     * 异常 handler map，存储从 throwable -> exception handler 的映射
     */
    private ExceptionMap mExceptionMap;

    /**
     * 主线程的异常处理
     */
    private ExceptionListener<Throwable> mMainThreadListener;

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

        if (thread == Looper.getMainLooper().getThread() && mMainThreadListener != null) {
            mMainThreadListener.handle(ex);
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

    public static void addMainThreadHandler(ExceptionListener<Throwable> listener) {
        Singleton.sInstance.addMainThreadHandlerMethod(listener);
    }

    private void addMainThreadHandlerMethod(ExceptionListener<Throwable> listener) {
        mMainThreadListener = listener;
    }

    private <T extends Throwable> void addHandlerMethod(Class<T> tClass, ExceptionListener<T> listener) {
        if (mExceptionMap.keySet().contains(tClass)) {
            Log.i("ErrorHandler", "Handler of " + tClass.getName() + " will be replaced");
        }
        //noinspection unchecked
        mExceptionMap.put(tClass, listener);
    }
}
