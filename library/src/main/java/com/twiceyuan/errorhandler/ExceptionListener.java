package com.twiceyuan.errorhandler;

/**
 * Created by twiceYuan on 6/13/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p/>
 * 自定义的异常监听器
 */
public interface ExceptionListener<T extends Throwable> {
    void handle(T t);
}
