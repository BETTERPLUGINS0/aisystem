package com.volmit.iris.util.decree.specialhandlers;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectHandler implements DecreeParameterHandler<String> {
   public KList<String> getPossibilities() {
      KList var1 = new KList();
      IrisData var2 = this.data();
      if (var2 != null) {
         return new KList(var2.getObjectLoader().getPossibleKeys());
      } else {
         File[] var3 = Iris.instance.getDataFolder(new String[]{"packs"}).listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (var6.isDirectory()) {
               var2 = IrisData.get(var6);
               var1.add((Object[])var2.getObjectLoader().getPossibleKeys());
            }
         }

         return var1;
      }
   }

   public String toString(String irisObject) {
      return var1;
   }

   public String parse(String in, boolean force) {
      KList var3 = this.getPossibilities(var1);
      if (var3.isEmpty()) {
         throw new DecreeParsingException("Unable to find Object \"" + var1 + "\"");
      } else {
         try {
            return (String)((List)var3.stream().filter((var2x) -> {
               return this.toString(var2x).equalsIgnoreCase(var1);
            }).collect(Collectors.toList())).get(0);
         } catch (Throwable var5) {
            throw new DecreeParsingException("Unable to filter which Object \"" + var1 + "\"");
         }
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(String.class);
   }

   public String getRandomDefault() {
      String var1 = (String)this.getPossibilities().getRandom();
      return var1 == null ? "object" : var1;
   }
}
