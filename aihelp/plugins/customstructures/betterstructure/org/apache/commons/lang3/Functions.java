/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;

public class Functions {
    public static <T extends Throwable> void run(FailableRunnable<T> pRunnable) {
        try {
            pRunnable.run();
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <O, T extends Throwable> O call(FailableCallable<O, T> pCallable) {
        try {
            return pCallable.call();
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <O, T extends Throwable> void accept(FailableConsumer<O, T> pConsumer, O pObject) {
        try {
            pConsumer.accept(pObject);
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <O1, O2, T extends Throwable> void accept(FailableBiConsumer<O1, O2, T> pConsumer, O1 pObject1, O2 pObject2) {
        try {
            pConsumer.accept(pObject1, pObject2);
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <I, O, T extends Throwable> O apply(FailableFunction<I, O, T> pFunction, I pInput) {
        try {
            return pFunction.apply(pInput);
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <I1, I2, O, T extends Throwable> O apply(FailableBiFunction<I1, I2, O, T> pFunction, I1 pInput1, I2 pInput2) {
        try {
            return pFunction.apply(pInput1, pInput2);
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <O, T extends Throwable> boolean test(FailablePredicate<O, T> pPredicate, O pObject) {
        try {
            return pPredicate.test(pObject);
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    public static <O1, O2, T extends Throwable> boolean test(FailableBiPredicate<O1, O2, T> pPredicate, O1 pObject1, O2 pObject2) {
        try {
            return pPredicate.test(pObject1, pObject2);
        } catch (Throwable t) {
            throw Functions.rethrow(t);
        }
    }

    @SafeVarargs
    public static void tryWithResources(FailableRunnable<? extends Throwable> pAction, FailableConsumer<Throwable, ? extends Throwable> pErrorHandler, FailableRunnable<? extends Throwable> ... pResources) {
        FailableConsumer<Throwable, Object> errorHandler = pErrorHandler == null ? t -> Functions.rethrow(t) : pErrorHandler;
        if (pResources != null) {
            for (FailableRunnable<? extends Throwable> runnable : pResources) {
                if (runnable != null) continue;
                throw new NullPointerException("A resource action must not be null.");
            }
        }
        Throwable th = null;
        try {
            pAction.run();
        } catch (Throwable t2) {
            th = t2;
        }
        if (pResources != null) {
            for (FailableRunnable<? extends Throwable> runnable : pResources) {
                try {
                    runnable.run();
                } catch (Throwable t3) {
                    if (th != null) continue;
                    th = t3;
                }
            }
        }
        if (th != null) {
            try {
                errorHandler.accept(th);
            } catch (Throwable t4) {
                throw Functions.rethrow(t4);
            }
        }
    }

    @SafeVarargs
    public static void tryWithResources(FailableRunnable<? extends Throwable> pAction, FailableRunnable<? extends Throwable> ... pResources) {
        Functions.tryWithResources(pAction, null, pResources);
    }

    public static RuntimeException rethrow(Throwable pThrowable) {
        if (pThrowable == null) {
            throw new NullPointerException("The Throwable must not be null.");
        }
        if (pThrowable instanceof RuntimeException) {
            throw (RuntimeException)pThrowable;
        }
        if (pThrowable instanceof Error) {
            throw (Error)pThrowable;
        }
        if (pThrowable instanceof IOException) {
            throw new UncheckedIOException((IOException)pThrowable);
        }
        throw new UndeclaredThrowableException(pThrowable);
    }

    @FunctionalInterface
    public static interface FailableBiPredicate<O1, O2, T extends Throwable> {
        public boolean test(O1 var1, O2 var2) throws T;
    }

    @FunctionalInterface
    public static interface FailablePredicate<O, T extends Throwable> {
        public boolean test(O var1) throws T;
    }

    @FunctionalInterface
    public static interface FailableBiFunction<I1, I2, O, T extends Throwable> {
        public O apply(I1 var1, I2 var2) throws T;
    }

    @FunctionalInterface
    public static interface FailableFunction<I, O, T extends Throwable> {
        public O apply(I var1) throws T;
    }

    @FunctionalInterface
    public static interface FailableBiConsumer<O1, O2, T extends Throwable> {
        public void accept(O1 var1, O2 var2) throws T;
    }

    @FunctionalInterface
    public static interface FailableConsumer<O, T extends Throwable> {
        public void accept(O var1) throws T;
    }

    @FunctionalInterface
    public static interface FailableCallable<O, T extends Throwable> {
        public O call() throws T;
    }

    @FunctionalInterface
    public static interface FailableRunnable<T extends Throwable> {
        public void run() throws T;
    }
}

