package me.PM2.infinitevehicles.xseries.reflection.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class AggregateReflectiveHandle<T, H extends ReflectiveHandle<T>> implements ReflectiveHandle<T> {
   private final List<Callable<H>> handles;
   private Consumer<H> handleModifier;

   @Internal
   public AggregateReflectiveHandle(Collection<Callable<H>> var1) {
      this.handles = new ArrayList(var1.size());
      this.handles.addAll(var1);
   }

   public AggregateReflectiveHandle<T, H> or(@NotNull H var1) {
      return this.or(() -> {
         return var1;
      });
   }

   public AggregateReflectiveHandle<T, H> or(@NotNull Callable<H> var1) {
      this.handles.add(var1);
      return this;
   }

   public AggregateReflectiveHandle<T, H> modify(@Nullable Consumer<H> var1) {
      this.handleModifier = var1;
      return this;
   }

   public H getHandle() {
      ClassNotFoundException var1 = null;
      Iterator var2 = this.handles.iterator();

      while(var2.hasNext()) {
         Callable var3 = (Callable)var2.next();

         try {
            ReflectiveHandle var4 = (ReflectiveHandle)var3.call();
            if (this.handleModifier != null) {
               this.handleModifier.accept(var4);
            }

            if (!var4.exists()) {
               var4.reflect();
            }

            return var4;
         } catch (Throwable var6) {
            if (var1 == null) {
               var1 = new ClassNotFoundException("None of the aggregate handles were successful");
            }

            var1.addSuppressed(var6);
         }
      }

      throw XReflection.throwCheckedException(XReflection.relativizeSuppressedExceptions(var1));
   }

   public AggregateReflectiveHandle<T, H> copy() {
      AggregateReflectiveHandle var1 = new AggregateReflectiveHandle(new ArrayList(this.handles));
      var1.handleModifier = this.handleModifier;
      return var1;
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return this.getHandle().jvm();
   }

   public T reflect() {
      ClassNotFoundException var1 = null;
      Iterator var2 = this.handles.iterator();

      while(var2.hasNext()) {
         Callable var3 = (Callable)var2.next();

         ReflectiveHandle var4;
         try {
            var4 = (ReflectiveHandle)var3.call();
            if (this.handleModifier != null) {
               this.handleModifier.accept(var4);
            }
         } catch (Throwable var7) {
            if (var1 == null) {
               var1 = new ClassNotFoundException("None of the aggregate handles were successful");
            }

            var1.addSuppressed(var7);
            continue;
         }

         try {
            return var4.reflect();
         } catch (Throwable var6) {
            if (var1 == null) {
               var1 = new ClassNotFoundException("None of the aggregate handles were successful");
            }

            var1.addSuppressed(var6);
         }
      }

      throw (ClassNotFoundException)XReflection.relativizeSuppressedExceptions(var1);
   }
}
