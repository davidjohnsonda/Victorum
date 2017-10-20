package io.github.victorum.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadingUtil{
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);

    private ThreadingUtil(){}
}
