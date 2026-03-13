package com.volmit.iris.util.data;

import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.util.collection.KMap;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public interface IrisCustomData extends BlockData {
   @NonNull
   BlockData getBase();

   @NonNull
   Identifier getCustom();

   static IrisCustomData of(@NotNull BlockData base, @NotNull Identifier custom) {
      Class<? extends BlockData> clazz = base.getClass();
      ClassLoader loader = IrisCustomData.class.getClassLoader();
      return (IrisCustomData)Proxy.newProxyInstance(loader, IrisCustomData.Internal.getInterfaces(loader, clazz), (proxy, method, args) -> {
         String var5 = method.getName();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -1295482945:
            if (var5.equals("equals")) {
               var6 = 6;
            }
            break;
         case -75665593:
            if (var5.equals("getBase")) {
               var6 = 0;
            }
            break;
         case 94756189:
            if (var5.equals("clone")) {
               var6 = 3;
            }
            break;
         case 103785528:
            if (var5.equals("merge")) {
               var6 = 2;
            }
            break;
         case 147696667:
            if (var5.equals("hashCode")) {
               var6 = 4;
            }
            break;
         case 346926695:
            if (var5.equals("getCustom")) {
               var6 = 1;
            }
            break;
         case 840862003:
            if (var5.equals("matches")) {
               var6 = 5;
            }
         }

         Object var10000;
         IrisCustomData store;
         Object patt0$temp;
         switch(var6) {
         case 0:
            var10000 = base;
            break;
         case 1:
            var10000 = custom;
            break;
         case 2:
            var10000 = of(base.merge((BlockData)args[0]), custom);
            break;
         case 3:
            var10000 = of(base.clone(), custom);
            break;
         case 4:
            var10000 = Objects.hash(new Object[]{base, custom});
            break;
         case 5:
            patt0$temp = args[0];
            if (patt0$temp instanceof IrisCustomData) {
               store = (IrisCustomData)patt0$temp;
               var10000 = base.matches(store.getBase()) && custom.equals(store.getCustom());
            } else {
               var10000 = false;
            }
            break;
         case 6:
            patt0$temp = args[0];
            if (patt0$temp instanceof IrisCustomData) {
               store = (IrisCustomData)patt0$temp;
               var10000 = store.getBase().equals(base) && store.getCustom().equals(custom);
            } else {
               var10000 = false;
            }
            break;
         default:
            var10000 = method.invoke(base, args);
         }

         return var10000;
      });
   }

   @org.jetbrains.annotations.ApiStatus.Internal
   public abstract static class Internal {
      private static final KMap<Class<?>, Class<?>[]> cache = new KMap();

      private static Class<?>[] getInterfaces(ClassLoader loader, Class<?> base) {
         return (Class[])cache.computeIfAbsent(var1, (var2) -> {
            HashSet var3 = new HashSet();

            for(Class var4 = var1; var4 != null && BlockData.class.isAssignableFrom(var4); var4 = var4.getSuperclass()) {
               Class[] var5 = var4.getInterfaces();
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  Class var8 = var5[var7];
                  if (!var8.isSealed() && !var8.isHidden()) {
                     try {
                        Class.forName(var8.getName(), false, var0);
                        var3.add(var8);
                     } catch (ClassNotFoundException var10) {
                     }
                  }
               }
            }

            var3.add(IrisCustomData.class);
            return (Class[])var3.toArray((var0x) -> {
               return new Class[var0x];
            });
         });
      }
   }
}
