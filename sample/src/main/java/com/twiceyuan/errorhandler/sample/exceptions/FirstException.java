package com.twiceyuan.errorhandler.sample.exceptions;

/**
 * Created by twiceYuan on 6/12/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class FirstException extends RuntimeException {
    public FirstException() {
        super("第一种自定义异常");
    }
}
