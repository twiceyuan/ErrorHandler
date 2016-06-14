# ErrorHandler

Android 全局异常处理，通过封装 Thread.UncaughtExceptionHandler 实现。通过方便的设定即可在全局对统一的异常做处理，并可以方便拦截主线程异常（FC），定制自己的 FC 界面。

## 使用

初始化

```java
ErrorHandler.init(this);
```

非主线程的异常处理（不会造成 FC 的异常），例如网络中断，服务端返回错误的自定义异常封装

```java
ErrorHandler.addHandler(FirstException.class, new ExceptionListener<SecondException>() {
    @Override public void handle(SecondException e) {
        Toast.makeText(App.getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
});
```

项目里默认提供了一个 ReportActivity 来接收主线程的异常信息。用户可以通过下面这个方法来设置自己的 FcHandlerActivity：

```java
void setMainThreadHandler(Class<Activity> activityClass);
```

然后在这个 Activity 中，可以通过`ErrorHandler.getThrowable(this)` 来获得 Throwable 对象，对其做自定义的处理。
