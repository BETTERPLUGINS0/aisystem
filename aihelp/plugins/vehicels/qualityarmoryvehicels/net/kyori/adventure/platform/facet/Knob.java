/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.facet;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Knob {
    private static final String NAMESPACE = "net.kyo".concat("ri.adventure");
    public static final boolean DEBUG = Knob.isEnabled("debug", false);
    private static final Set<Object> UNSUPPORTED = new CopyOnWriteArraySet<Object>();
    public static volatile Consumer<String> OUT = System.out::println;
    public static volatile BiConsumer<String, Throwable> ERR = (string, throwable) -> {
        System.err.println((String)string);
        if (throwable != null) {
            throwable.printStackTrace(System.err);
        }
    };

    private Knob() {
    }

    public static boolean isEnabled(@NotNull String string, boolean bl) {
        return System.getProperty(NAMESPACE + "." + string, Boolean.toString(bl)).equalsIgnoreCase("true");
    }

    public static void logError(@Nullable Throwable throwable, @NotNull String string, @NotNull Object ... objectArray) {
        if (DEBUG) {
            ERR.accept(String.format(string, objectArray), throwable);
        }
    }

    public static void logMessage(@NotNull String string, @NotNull Object ... objectArray) {
        if (DEBUG) {
            OUT.accept(String.format(string, objectArray));
        }
    }

    public static void logUnsupported(@NotNull Object object, @NotNull Object object2) {
        if (DEBUG && UNSUPPORTED.add(object2)) {
            OUT.accept(String.format("Unsupported value '%s' for facet: %s", object2, object));
        }
    }
}

