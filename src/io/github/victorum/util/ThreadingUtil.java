package io.github.victorum.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadingUtil{
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*8);
    public static final ExecutorService IO_SERVICE = Executors.newSingleThreadExecutor();

    private ThreadingUtil(){}
}
