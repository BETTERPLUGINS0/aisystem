package org.terraform.utils.version;

import java.lang.reflect.Method;
import java.util.StringJoiner;

public class TerraformMethodHandler {
   public final Method method;

   public TerraformMethodHandler(Class<?> host, String[] possibleNames, Class<?>... parameters) throws NoSuchMethodException, SecurityException {
      Method tryMethod = null;
      boolean bound = false;
      String[] var6 = possibleNames;
      int var7 = possibleNames.length;

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         String name = var6[var8];

         try {
            tryMethod = host.getDeclaredMethod(name, parameters);
            tryMethod.setAccessible(true);
            bound = true;
         } catch (NoSuchMethodException var11) {
         }
      }

      if (bound) {
         this.method = tryMethod;
      } else {
         this.method = null;
         StringJoiner names = new StringJoiner(",");
         String[] var13 = possibleNames;
         var8 = possibleNames.length;

         for(int var14 = 0; var14 < var8; ++var14) {
            String name = var13[var14];
            names.add(name);
         }

         String var10002 = String.valueOf(host);
         throw new NoSuchMethodException("No method in class " + var10002 + " named [" + String.valueOf(names) + "]");
      }
   }
}
