package me.PM2.infinitevehicles.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

public abstract class MessageFormatter<FT> {
   private final List<FT> colors = new ArrayList();

   @SafeVarargs
   public MessageFormatter(FT... colors) {
      this.colors.addAll(Arrays.asList(var1));
   }

   public FT setColor(int index, FT color) {
      if (var1 > 0) {
         --var1;
      } else {
         var1 = 0;
      }

      if (this.colors.size() <= var1) {
         int var3 = var1 - this.colors.size();
         if (var3 > 0) {
            this.colors.addAll(Collections.nCopies(var3, (Object)null));
         }

         this.colors.add(var2);
         return null;
      } else {
         return this.colors.set(var1, var2);
      }
   }

   public FT getColor(int index) {
      if (var1 > 0) {
         --var1;
      } else {
         var1 = 0;
      }

      Object var2 = this.colors.get(var1);
      if (var2 == null) {
         var2 = this.getDefaultColor();
      }

      return var2;
   }

   public FT getDefaultColor() {
      return this.getColor(1);
   }

   abstract String format(FT color, String message);

   public String format(int index, String message) {
      return this.format(this.getColor(var1), var2);
   }

   public String format(String message) {
      String var2 = this.format(1, "");
      Matcher var3 = ACFPatterns.FORMATTER.matcher(var1);
      StringBuffer var4 = new StringBuffer(var1.length());

      while(var3.find()) {
         Integer var5 = ACFUtil.parseInt(var3.group("color"), 1);
         String var6 = this.format(var5, var3.group("msg")) + var2;
         var3.appendReplacement(var4, Matcher.quoteReplacement(var6));
      }

      var3.appendTail(var4);
      return var2 + var4.toString();
   }
}
