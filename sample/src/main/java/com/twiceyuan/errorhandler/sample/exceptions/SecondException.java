package com.twiceyuan.errorhandler.sample.exceptions;

/**
 * Created by twiceYuan on 6/12/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class SecondException extends RuntimeException {
    public SecondException() {
        super("第二种自定义异常");
    }
}
