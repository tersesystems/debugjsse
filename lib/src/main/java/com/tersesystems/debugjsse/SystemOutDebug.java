package com.tersesystems.debugjsse;

/**
 * This class writes to System.out for debugging output.
 */
public class SystemOutDebug extends PrintStreamDebug {
    public SystemOutDebug() {
        super(System.out);
    }
}
