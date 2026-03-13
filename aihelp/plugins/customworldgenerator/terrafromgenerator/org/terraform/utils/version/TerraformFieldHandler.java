package org.terraform.utils.version;

import java.lang.reflect.Field;
import java.util.StringJoiner;

public class TerraformFieldHandler {
   public final Field field;

   public TerraformFieldHandler(Class<?> host, String... possibleNames) throws NoSuchFieldException, SecurityException {
      Field tryField = null;
      boolean bound = false;
      String[] var5 = possibleNames;
      int var6 = possibleNames.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         String name = var5[var7];

         try {
            tryField = host.getDeclaredField(name);
            tryField.setAccessible(true);
            bound = true;
         } catch (NoSuchFieldException var10) {
         }
      }

      if (bound) {
         this.field = tryField;
      } else {
         this.field = null;
         StringJoiner names = new StringJoiner(",");
         String[] var12 = possibleNames;
         var7 = possibleNames.length;

         for(int var13 = 0; var13 < var7; ++var13) {
            String name = var12[var13];
            names.add(name);
         }

         String var10002 = String.valueOf(host);
         throw new NoSuchFieldException("No field in class " + var10002 + " named [" + String.valueOf(names) + "]");
      }
   }
}
