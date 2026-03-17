/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.mojang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.ProfileLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class PlayerProfileFetcherThread
implements ThreadFactory {
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2, new PlayerProfileFetcherThread());
    private static final AtomicInteger COUNT = new AtomicInteger();

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        Thread thread2 = new Thread(runnable);
        thread2.setName("Profile Lookup Executor #" + COUNT.getAndIncrement());
        thread2.setUncaughtExceptionHandler((thread, throwable) -> ProfileLogger.LOGGER.error("Uncaught exception in thread {}", (Object)thread.getName(), (Object)throwable));
        return thread2;
    }
}

