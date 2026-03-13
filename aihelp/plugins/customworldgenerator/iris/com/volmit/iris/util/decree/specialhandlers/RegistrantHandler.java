package com.volmit.iris.util.decree.specialhandlers;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

public abstract class RegistrantHandler<T extends IrisRegistrant> implements DecreeParameterHandler<T> {
   private final Class<T> type;
   private final String name;
   private final boolean nullable;

   public RegistrantHandler(Class<T> type, boolean nullable) {
      this.type = var1;
      this.name = var1.getSimpleName().replaceFirst("Iris", "");
      this.nullable = var2;
   }

   public KList<T> getPossibilities() {
      KList var1 = new KList();
      HashSet var2 = new HashSet();
      IrisData var3 = this.data();
      if (var3 != null) {
         Iterator var4 = var3.getLoader(this.type).loadAll(var3.getLoader(this.type).getPossibleKeys()).iterator();

         while(var4.hasNext()) {
            IrisRegistrant var5 = (IrisRegistrant)var4.next();
            var2.add(var5.getLoadKey());
            var1.add((Object)var5);
         }
      }

      File[] var10 = Iris.instance.getDataFolder(new String[]{"packs"}).listFiles();
      int var11 = var10.length;

      for(int var6 = 0; var6 < var11; ++var6) {
         File var7 = var10[var6];
         if (var7.isDirectory()) {
            var3 = IrisData.get(var7);
            Iterator var8 = var3.getLoader(this.type).loadAll(var3.getLoader(this.type).getPossibleKeys()).iterator();

            while(var8.hasNext()) {
               IrisRegistrant var9 = (IrisRegistrant)var8.next();
               if (var2.add(var9.getLoadKey())) {
                  var1.add((Object)var9);
               }
            }
         }
      }

      return var1;
   }

   public String toString(T t) {
      return var1 != null ? var1.getLoadKey() : "null";
   }

   public T parse(String in, boolean force) {
      if (var1.equals("null") && this.nullable) {
         return null;
      } else {
         KList var3 = this.getPossibilities(var1);
         if (var3.isEmpty()) {
            throw new DecreeParsingException("Unable to find " + this.name + " \"" + var1 + "\"");
         } else {
            return (IrisRegistrant)var3.stream().filter((var2x) -> {
               return this.toString(var2x).equalsIgnoreCase(var1);
            }).findFirst().orElseThrow(() -> {
               return new DecreeParsingException("Unable to filter which " + this.name + " \"" + var1 + "\"");
            });
         }
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(this.type);
   }
}
