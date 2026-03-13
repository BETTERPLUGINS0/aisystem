package com.volmit.iris.util.slimjar.injector.loader.factory;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class SelectingInjectableFactory implements InjectableFactory {
   @NotNull
   private final InjectableFactory fallback;
   @NotNull
   private final List<InjectableFactory> factories;

   public SelectingInjectableFactory(@NotNull InjectableFactory var1, @NotNull InjectableFactory... var2) {
      if (var2.length == 0) {
         throw new IllegalArgumentException("No factories provided");
      } else {
         this.fallback = var1;
         this.factories = List.of(var2);
      }
   }

   @NotNull
   public Injectable create(@NotNull Path var1, @NotNull Collection<Repository> var2, @NotNull ClassLoader var3) {
      for(ClassLoader var4 = var3; var4 != null; var4 = var4.getParent()) {
         Iterator var5 = this.factories.iterator();

         while(var5.hasNext()) {
            InjectableFactory var6 = (InjectableFactory)var5.next();

            try {
               return var6.create(var1, var2, var4);
            } catch (InjectorException var8) {
            }
         }
      }

      return this.fallback.create(var1, var2, var3);
   }
}
