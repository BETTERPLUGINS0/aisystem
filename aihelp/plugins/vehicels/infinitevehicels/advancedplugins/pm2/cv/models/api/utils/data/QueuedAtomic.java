package advancedplugins.pm2.cv.models.api.utils.data;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class QueuedAtomic<T> {
   private final AtomicReference<T> writer = new AtomicReference();
   private final AtomicReference<T> reader = new AtomicReference();
   private final BiConsumer<T, AtomicReference<T>> setter;
   private final Function<AtomicReference<T>, T> getter;
   private final BiConsumer<AtomicReference<T>, AtomicReference<T>> passer;

   public QueuedAtomic(Supplier<T> var1, BiConsumer<T, AtomicReference<T>> var2, Function<AtomicReference<T>, T> var3, BiConsumer<AtomicReference<T>, AtomicReference<T>> var4) {
      this.writer.set(var1.get());
      this.reader.set(var1.get());
      this.setter = var2;
      this.getter = var3;
      this.passer = var4;
   }

   public static <T> QueuedAtomic.Builder<T> builder() {
      return new QueuedAtomic.Builder();
   }

   public void set(T var1) {
      this.setter.accept(var1, this.writer);
   }

   public void pass() {
      this.passer.accept(this.writer, this.reader);
   }

   public T get() {
      return this.getter.apply(this.reader);
   }

   public T getUnsafe() {
      return this.getter.apply(this.writer);
   }

   public void setUnsafe(T var1) {
      this.setter.accept(var1, this.reader);
   }

   public static class Builder<T> {
      private Supplier<T> value = () -> {
         return null;
      };
      private BiConsumer<T, AtomicReference<T>> setter = (var0, var1) -> {
         var1.set(var0);
      };
      private Function<AtomicReference<T>, T> getter = AtomicReference::get;
      private BiConsumer<AtomicReference<T>, AtomicReference<T>> passer = (var0, var1) -> {
         var1.set(var0.get());
      };

      public QueuedAtomic.Builder<T> value(Supplier<T> var1) {
         this.value = var1;
         return this;
      }

      public QueuedAtomic.Builder<T> setter(BiConsumer<T, AtomicReference<T>> var1) {
         this.setter = var1;
         return this;
      }

      public QueuedAtomic.Builder<T> getter(Function<AtomicReference<T>, T> var1) {
         this.getter = var1;
         return this;
      }

      public QueuedAtomic.Builder<T> passer(BiConsumer<AtomicReference<T>, AtomicReference<T>> var1) {
         this.passer = var1;
         return this;
      }

      public QueuedAtomic<T> build() {
         return new QueuedAtomic(this.value, this.setter, this.getter, this.passer);
      }
   }
}
