package com.tersesystems.debugjsse;

public class SystemOutDebug implements Debug {
    @Override
    public void enter(String message) {
        System.out.println("enter: message = [" + message + "]");
    }

    @Override
    public void exit(String message, Object result) {
        System.out.println("exit: message = " + message + ", result = " + result);
    }

    @Override
    public void exception(String message, Exception e) {
        System.out.println("exception: message = [" + message + "], e = [" + e + "]");
    }
}
