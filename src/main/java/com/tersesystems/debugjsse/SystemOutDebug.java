package com.tersesystems.debugjsse;

public class SystemOutDebug extends PrintStreamDebug {
    public SystemOutDebug() {
        super(System.out);
    }
}
