package com.tersesystems.debugjsse;


public interface Debug {
    void enter(String message);

    void exit(String message, Object result);

    void exception(String message, Exception e);
}
