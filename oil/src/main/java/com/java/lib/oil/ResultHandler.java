package com.java.lib.oil;

/**
 * Created by wb-duanyongyu on 2016/2/3.
 */
public interface ResultHandler {
    public boolean onSuccess(String method, Object result, Object... args);
    public boolean onFail(String method, Exception exception, Object... args);
}
