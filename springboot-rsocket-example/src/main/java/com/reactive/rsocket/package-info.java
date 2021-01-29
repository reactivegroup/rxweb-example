package com.reactive.rsocket;

/**
 * Thread printer helper.
 */
class ThreadPrinter {

    /**
     * Print current thread stack.
     */
    static String printThreadStack() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        StringBuilder sbf = new StringBuilder();
        for (StackTraceElement e : st) {
            if (sbf.length() > 0) {
                sbf.append(" <- ");
                sbf.append(System.getProperty("line.separator"));
            }
            sbf.append(java.text.MessageFormat.format("{0}.{1}() {2}"
                    , e.getClassName()
                    , e.getMethodName()
                    , e.getLineNumber()));
        }
        return sbf.toString();
    }
}