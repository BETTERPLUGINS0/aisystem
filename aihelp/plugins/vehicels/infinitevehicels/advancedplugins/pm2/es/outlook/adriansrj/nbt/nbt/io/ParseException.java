package es.outlook.adriansrj.nbt.nbt.io;

import java.io.IOException;

public class ParseException extends IOException {
   public ParseException(String var1) {
      super(var1);
   }

   public ParseException(String var1, String var2, int var3) {
      super(var1 + " at: " + formatError(var2, var3));
   }

   private static String formatError(String var0, int var1) {
      StringBuilder var2 = new StringBuilder();
      int var3 = Math.min(var0.length(), var1);
      if (var3 > 35) {
         var2.append("...");
      }

      var2.append(var0, Math.max(0, var3 - 35), var3);
      var2.append("<--[HERE]");
      return var2.toString();
   }
}
