package fr.xephi.authme.libs.net.kyori.adventure.platform.facet;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Knob {
   private static final String NAMESPACE = "net.kyo".concat("ri.adventure");
   public static final boolean DEBUG = isEnabled("debug", false);
   private static final Set<Object> UNSUPPORTED = new CopyOnWriteArraySet();
   public static volatile Consumer<String> OUT;
   public static volatile BiConsumer<String, Throwable> ERR;

   private Knob() {
   }

   public static boolean isEnabled(@NotNull final String key, final boolean defaultValue) {
      return System.getProperty(NAMESPACE + "." + key, Boolean.toString(defaultValue)).equalsIgnoreCase("true");
   }

   public static void logError(@Nullable final Throwable error, @NotNull final String format, @NotNull final Object... arguments) {
      if (DEBUG) {
         ERR.accept(String.format(format, arguments), error);
      }

   }

   public static void logMessage(@NotNull final String format, @NotNull final Object... arguments) {
      if (DEBUG) {
         OUT.accept(String.format(format, arguments));
      }

   }

   public static void logUnsupported(@NotNull final Object facet, @NotNull final Object value) {
      if (DEBUG && UNSUPPORTED.add(value)) {
         OUT.accept(String.format("Unsupported value '%s' for facet: %s", value, facet));
      }

   }

   static {
      PrintStream var10000 = System.out;
      Objects.requireNonNull(var10000);
      OUT = var10000::println;
      ERR = (message, err) -> {
         System.err.println(message);
         if (err != null) {
            err.printStackTrace(System.err);
         }

      };
   }
}
