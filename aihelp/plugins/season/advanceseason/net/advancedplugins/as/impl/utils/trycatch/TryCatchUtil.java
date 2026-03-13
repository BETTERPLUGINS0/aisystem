/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.trycatch;

import java.util.function.Consumer;

public class TryCatchUtil {
    public static <T> T tryOrDefault(ITryCatchWithReturn<T> iTryCatchWithReturn, T t, Consumer<Exception> consumer) {
        try {
            return iTryCatchWithReturn.run();
        } catch (Exception exception) {
            if (consumer != null) {
                consumer.accept(exception);
            } else {
                exception.printStackTrace();
            }
            return t;
        }
    }

    public static <T> T tryOrDefault(ITryCatchWithReturn<T> iTryCatchWithReturn, T t) {
        return TryCatchUtil.tryOrDefault(iTryCatchWithReturn, t, null);
    }

    public static <T> T tryAndReturn(ITryCatchWithReturn<T> iTryCatchWithReturn) {
        return TryCatchUtil.tryOrDefault(iTryCatchWithReturn, null);
    }

    public static void tryRun(ITryCatch iTryCatch, Consumer<Exception> consumer) {
        TryCatchUtil.tryOrDefault(() -> {
            iTryCatch.run();
            return null;
        }, null, consumer);
    }

    public static void tryRun(ITryCatch iTryCatch) {
        TryCatchUtil.tryRun(iTryCatch, null);
    }

    @FunctionalInterface
    public static interface ITryCatchWithReturn<T> {
        public T run() throws Exception;
    }

    @FunctionalInterface
    public static interface ITryCatch {
        public void run() throws Exception;
    }
}

