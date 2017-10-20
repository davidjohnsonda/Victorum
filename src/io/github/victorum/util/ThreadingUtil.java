package io.github.victorum.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadingUtil{
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    private ThreadingUtil(){}
}
