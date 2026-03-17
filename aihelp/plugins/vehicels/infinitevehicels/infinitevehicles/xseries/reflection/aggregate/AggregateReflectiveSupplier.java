package me.PM2.infinitevehicles.xseries.reflection.aggregate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Experimental
public class AggregateReflectiveSupplier<H extends ReflectiveHandle<?>, O> {
   private final List<AggregateReflectiveSupplier<H, O>.ReflectivePair> handles = new ArrayList();
   private Consumer<H> handleModifier;

   public AggregateReflectiveSupplier<H, O> or(@NotNull H var1, O var2) {
      return this.or(() -> {
         return var1;
      }, var2);
   }

   public AggregateReflectiveSupplier<H, O> or(@NotNull Callable<H> var1, O var2) {
      return this.or(var1, () -> {
         return var2;
      });
   }

   public AggregateReflectiveSupplier<H, O> or(@NotNull H var1, Supplier<O> var2) {
      return this.or(() -> {
         return var1;
      }, var2);
   }

   public AggregateReflectiveSupplier<H, O> or(@NotNull Callable<H> var1, Supplier<O> var2) {
      this.handles.add(new AggregateReflectiveSupplier.ReflectivePair(var1, var2));
      return this;
   }

   public AggregateReflectiveSupplier<H, O> modify(@Nullable Consumer<H> var1) {
      this.handleModifier = var1;
      return this;
   }

   public O get() {
      ClassNotFoundException var1 = null;
      Iterator var2 = this.handles.iterator();

      while(var2.hasNext()) {
         AggregateReflectiveSupplier.ReflectivePair var3 = (AggregateReflectiveSupplier.ReflectivePair)var2.next();

         try {
            ReflectiveHandle var4 = (ReflectiveHandle)var3.handle.call();
            if (this.handleModifier != null) {
               this.handleModifier.accept(var4);
            }

            if (!var4.exists()) {
               var4.reflect();
            }

            return var3.object.get();
         } catch (Throwable var6) {
            if (var1 == null) {
               var1 = new ClassNotFoundException("None of the aggregate handles were successful");
            }

            var1.addSuppressed(var6);
         }
      }

      throw XReflection.throwCheckedException(XReflection.relativizeSuppressedExceptions(var1));
   }

   private final class ReflectivePair {
      private final Callable<H> handle;
      private final Supplier<O> object;

      private ReflectivePair(Callable<H> param2, Supplier<O> param3) {
         this.handle = var2;
         this.object = var3;
      }

      // $FF: synthetic method
      ReflectivePair(Callable var2, Supplier var3, Object var4) {
         this(var2, var3);
      }
   }
}
