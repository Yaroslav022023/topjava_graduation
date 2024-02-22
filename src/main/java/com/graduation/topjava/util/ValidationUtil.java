package com.graduation.topjava.util;


import com.graduation.topjava.util.exception.ExistException;
import com.graduation.topjava.util.exception.NotFoundException;


public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkDuplicate(boolean found, String msg) {
        if (found) {
            throw new ExistException(msg);
        }
    }
}