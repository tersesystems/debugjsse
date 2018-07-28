package com.tersesystems.debugjsse;

import java.io.PrintStream;

public class PrintStreamDebug extends AbstractDebug {

    private final PrintStream stream;

    public PrintStreamDebug(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void enter(String message) {
        stream.println(message);
    }

    @Override
    public void exit(String message) {
        stream.println(message);
    }

    @Override
    public void exception(String message, Exception e) {
        stream.println(message);
        e.printStackTrace(stream);
    }
}
