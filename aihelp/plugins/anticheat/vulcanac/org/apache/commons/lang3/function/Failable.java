package org.apache.commons.lang3.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.stream.Streams;

public class Failable {
   public static <T, U, E extends Throwable> void accept(FailableBiConsumer<T, U, E> var0, T var1, U var2) {
      run(() -> {
         var0.accept(var1, var2);
      });
   }

   public static <T, E extends Throwable> void accept(FailableConsumer<T, E> var0, T var1) {
      run(() -> {
         var0.accept(var1);
      });
   }

   public static <E extends Throwable> void accept(FailableDoubleConsumer<E> var0, double var1) {
      run(() -> {
         var0.accept(var1);
      });
   }

   public static <E extends Throwable> void accept(FailableIntConsumer<E> var0, int var1) {
      run(() -> {
         var0.accept(var1);
      });
   }

   public static <E extends Throwable> void accept(FailableLongConsumer<E> var0, long var1) {
      run(() -> {
         var0.accept(var1);
      });
   }

   public static <T, U, R, E extends Throwable> R apply(FailableBiFunction<T, U, R, E> var0, T var1, U var2) {
      return get(() -> {
         return var0.apply(var1, var2);
      });
   }

   public static <T, R, E extends Throwable> R apply(FailableFunction<T, R, E> var0, T var1) {
      return get(() -> {
         return var0.apply(var1);
      });
   }

   public static <E extends Throwable> double applyAsDouble(FailableDoubleBinaryOperator<E> var0, double var1, double var3) {
      return getAsDouble(() -> {
         return var0.applyAsDouble(var1, var3);
      });
   }

   public static <T, U> BiConsumer<T, U> asBiConsumer(FailableBiConsumer<T, U, ?> var0) {
      return (var1, var2) -> {
         accept(var0, var1, var2);
      };
   }

   public static <T, U, R> BiFunction<T, U, R> asBiFunction(FailableBiFunction<T, U, R, ?> var0) {
      return (var1, var2) -> {
         return apply(var0, var1, var2);
      };
   }

   public static <T, U> BiPredicate<T, U> asBiPredicate(FailableBiPredicate<T, U, ?> var0) {
      return (var1, var2) -> {
         return test(var0, var1, var2);
      };
   }

   public static <V> Callable<V> asCallable(FailableCallable<V, ?> var0) {
      return () -> {
         return call(var0);
      };
   }

   public static <T> Consumer<T> asConsumer(FailableConsumer<T, ?> var0) {
      return (var1) -> {
         accept(var0, var1);
      };
   }

   public static <T, R> Function<T, R> asFunction(FailableFunction<T, R, ?> var0) {
      return (var1) -> {
         return apply(var0, var1);
      };
   }

   public static <T> Predicate<T> asPredicate(FailablePredicate<T, ?> var0) {
      return (var1) -> {
         return test(var0, var1);
      };
   }

   public static Runnable asRunnable(FailableRunnable<?> var0) {
      return () -> {
         run(var0);
      };
   }

   public static <T> Supplier<T> asSupplier(FailableSupplier<T, ?> var0) {
      return () -> {
         return get(var0);
      };
   }

   public static <V, E extends Throwable> V call(FailableCallable<V, E> var0) {
      var0.getClass();
      return get(var0::call);
   }

   public static <T, E extends Throwable> T get(FailableSupplier<T, E> var0) {
      try {
         return var0.get();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <E extends Throwable> boolean getAsBoolean(FailableBooleanSupplier<E> var0) {
      try {
         return var0.getAsBoolean();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <E extends Throwable> double getAsDouble(FailableDoubleSupplier<E> var0) {
      try {
         return var0.getAsDouble();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <E extends Throwable> int getAsInt(FailableIntSupplier<E> var0) {
      try {
         return var0.getAsInt();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <E extends Throwable> long getAsLong(FailableLongSupplier<E> var0) {
      try {
         return var0.getAsLong();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <E extends Throwable> short getAsShort(FailableShortSupplier<E> var0) {
      try {
         return var0.getAsShort();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static RuntimeException rethrow(Throwable var0) {
      Objects.requireNonNull(var0, "throwable");
      if (var0 instanceof RuntimeException) {
         throw (RuntimeException)var0;
      } else if (var0 instanceof Error) {
         throw (Error)var0;
      } else if (var0 instanceof IOException) {
         throw new UncheckedIOException((IOException)var0);
      } else {
         throw new UndeclaredThrowableException(var0);
      }
   }

   public static <E extends Throwable> void run(FailableRunnable<E> var0) {
      try {
         var0.run();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <E> Streams.FailableStream<E> stream(Collection<E> var0) {
      return new Streams.FailableStream(var0.stream());
   }

   public static <T> Streams.FailableStream<T> stream(Stream<T> var0) {
      return new Streams.FailableStream(var0);
   }

   public static <T, U, E extends Throwable> boolean test(FailableBiPredicate<T, U, E> var0, T var1, U var2) {
      return getAsBoolean(() -> {
         return var0.test(var1, var2);
      });
   }

   public static <T, E extends Throwable> boolean test(FailablePredicate<T, E> var0, T var1) {
      return getAsBoolean(() -> {
         return var0.test(var1);
      });
   }

   @SafeVarargs
   public static void tryWithResources(FailableRunnable<? extends Throwable> var0, FailableConsumer<Throwable, ? extends Throwable> var1, FailableRunnable<? extends Throwable>... var2) {
      FailableConsumer var3;
      if (var1 == null) {
         var3 = Failable::rethrow;
      } else {
         var3 = var1;
      }

      int var6;
      if (var2 != null) {
         FailableRunnable[] var4 = var2;
         int var5 = var2.length;

         for(var6 = 0; var6 < var5; ++var6) {
            FailableRunnable var7 = var4[var6];
            Objects.requireNonNull(var7, "runnable");
         }
      }

      Throwable var13 = null;

      try {
         var0.run();
      } catch (Throwable var11) {
         var13 = var11;
      }

      if (var2 != null) {
         FailableRunnable[] var14 = var2;
         var6 = var2.length;

         for(int var15 = 0; var15 < var6; ++var15) {
            FailableRunnable var8 = var14[var15];

            try {
               var8.run();
            } catch (Throwable var12) {
               if (var13 == null) {
                  var13 = var12;
               }
            }
         }
      }

      if (var13 != null) {
         try {
            var3.accept(var13);
         } catch (Throwable var10) {
            throw rethrow(var10);
         }
      }

   }

   @SafeVarargs
   public static void tryWithResources(FailableRunnable<? extends Throwable> var0, FailableRunnable<? extends Throwable>... var1) {
      tryWithResources(var0, (FailableConsumer)null, var1);
   }

   private Failable() {
   }
}
