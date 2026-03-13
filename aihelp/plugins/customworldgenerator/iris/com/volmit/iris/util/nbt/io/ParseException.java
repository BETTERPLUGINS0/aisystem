package com.volmit.iris.util.nbt.io;

import java.io.IOException;

public class ParseException extends IOException {
   public ParseException(String msg) {
      super(var1);
   }

   public ParseException(String msg, String value, int index) {
      super(var1 + " at: " + formatError(var2, var3));
   }

   private static String formatError(String value, int index) {
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
