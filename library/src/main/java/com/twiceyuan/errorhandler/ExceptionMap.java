package com.twiceyuan.errorhandler;

import java.util.HashMap;

/**
 * Created by twiceYuan on 6/13/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class ExceptionMap<T extends Throwable> extends HashMap<Class<T>, ExceptionListener<T>> {
}
