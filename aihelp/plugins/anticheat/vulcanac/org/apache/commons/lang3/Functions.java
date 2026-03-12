package org.apache.commons.lang3;

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
import org.apache.commons.lang3.function.FailableBooleanSupplier;

/** @deprecated */
@Deprecated
public class Functions {
   public static <O1, O2, T extends Throwable> void accept(Functions.FailableBiConsumer<O1, O2, T> var0, O1 var1, O2 var2) {
      run(() -> {
         var0.accept(var1, var2);
      });
   }

   public static <O, T extends Throwable> void accept(Functions.FailableConsumer<O, T> var0, O var1) {
      run(() -> {
         var0.accept(var1);
      });
   }

   public static <O1, O2, O, T extends Throwable> O apply(Functions.FailableBiFunction<O1, O2, O, T> var0, O1 var1, O2 var2) {
      return get(() -> {
         return var0.apply(var1, var2);
      });
   }

   public static <I, O, T extends Throwable> O apply(Functions.FailableFunction<I, O, T> var0, I var1) {
      return get(() -> {
         return var0.apply(var1);
      });
   }

   public static <O1, O2> BiConsumer<O1, O2> asBiConsumer(Functions.FailableBiConsumer<O1, O2, ?> var0) {
      return (var1, var2) -> {
         accept(var0, var1, var2);
      };
   }

   public static <O1, O2, O> BiFunction<O1, O2, O> asBiFunction(Functions.FailableBiFunction<O1, O2, O, ?> var0) {
      return (var1, var2) -> {
         return apply(var0, var1, var2);
      };
   }

   public static <O1, O2> BiPredicate<O1, O2> asBiPredicate(Functions.FailableBiPredicate<O1, O2, ?> var0) {
      return (var1, var2) -> {
         return test(var0, var1, var2);
      };
   }

   public static <O> Callable<O> asCallable(Functions.FailableCallable<O, ?> var0) {
      return () -> {
         return call(var0);
      };
   }

   public static <I> Consumer<I> asConsumer(Functions.FailableConsumer<I, ?> var0) {
      return (var1) -> {
         accept(var0, var1);
      };
   }

   public static <I, O> Function<I, O> asFunction(Functions.FailableFunction<I, O, ?> var0) {
      return (var1) -> {
         return apply(var0, var1);
      };
   }

   public static <I> Predicate<I> asPredicate(Functions.FailablePredicate<I, ?> var0) {
      return (var1) -> {
         return test(var0, var1);
      };
   }

   public static Runnable asRunnable(Functions.FailableRunnable<?> var0) {
      return () -> {
         run(var0);
      };
   }

   public static <O> Supplier<O> asSupplier(Functions.FailableSupplier<O, ?> var0) {
      return () -> {
         return get(var0);
      };
   }

   public static <O, T extends Throwable> O call(Functions.FailableCallable<O, T> var0) {
      var0.getClass();
      return get(var0::call);
   }

   public static <O, T extends Throwable> O get(Functions.FailableSupplier<O, T> var0) {
      try {
         return var0.get();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   private static <T extends Throwable> boolean getAsBoolean(FailableBooleanSupplier<T> var0) {
      try {
         return var0.getAsBoolean();
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

   public static <T extends Throwable> void run(Functions.FailableRunnable<T> var0) {
      try {
         var0.run();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static <O> Streams.FailableStream<O> stream(Collection<O> var0) {
      return new Streams.FailableStream(var0.stream());
   }

   public static <O> Streams.FailableStream<O> stream(Stream<O> var0) {
      return new Streams.FailableStream(var0);
   }

   public static <O1, O2, T extends Throwable> boolean test(Functions.FailableBiPredicate<O1, O2, T> var0, O1 var1, O2 var2) {
      return getAsBoolean(() -> {
         return var0.test(var1, var2);
      });
   }

   public static <O, T extends Throwable> boolean test(Functions.FailablePredicate<O, T> var0, O var1) {
      return getAsBoolean(() -> {
         return var0.test(var1);
      });
   }

   @SafeVarargs
   public static void tryWithResources(Functions.FailableRunnable<? extends Throwable> var0, Functions.FailableConsumer<Throwable, ? extends Throwable> var1, Functions.FailableRunnable<? extends Throwable>... var2) {
      Functions.FailableConsumer var3;
      if (var1 == null) {
         var3 = Functions::rethrow;
      } else {
         var3 = var1;
      }

      int var6;
      if (var2 != null) {
         Functions.FailableRunnable[] var4 = var2;
         int var5 = var2.length;

         for(var6 = 0; var6 < var5; ++var6) {
            Functions.FailableRunnable var7 = var4[var6];
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
         Functions.FailableRunnable[] var14 = var2;
         var6 = var2.length;

         for(int var15 = 0; var15 < var6; ++var15) {
            Functions.FailableRunnable var8 = var14[var15];

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
   public static void tryWithResources(Functions.FailableRunnable<? extends Throwable> var0, Functions.FailableRunnable<? extends Throwable>... var1) {
      tryWithResources(var0, (Functions.FailableConsumer)null, var1);
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableSupplier<R, T extends Throwable> {
      R get() throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableRunnable<T extends Throwable> {
      void run() throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailablePredicate<I, T extends Throwable> {
      boolean test(I var1) throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableFunction<I, R, T extends Throwable> {
      R apply(I var1) throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableConsumer<O, T extends Throwable> {
      void accept(O var1) throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableCallable<R, T extends Throwable> {
      R call() throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableBiPredicate<O1, O2, T extends Throwable> {
      boolean test(O1 var1, O2 var2) throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableBiFunction<O1, O2, R, T extends Throwable> {
      R apply(O1 var1, O2 var2) throws T;
   }

   /** @deprecated */
   @Deprecated
   @FunctionalInterface
   public interface FailableBiConsumer<O1, O2, T extends Throwable> {
      void accept(O1 var1, O2 var2) throws T;
   }
}
