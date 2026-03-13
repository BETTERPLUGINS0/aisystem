package com.volmit.iris.util.board;

import com.volmit.iris.util.format.C;
import lombok.Generated;
import org.apache.commons.lang.StringUtils;

public class BoardEntry {
   private final String prefix;
   private final String suffix;

   private BoardEntry(final String prefix, final String suffix) {
      this.prefix = var1;
      this.suffix = var2;
   }

   public static BoardEntry translateToEntry(String input) {
      if (var0.isEmpty()) {
         return new BoardEntry("", "");
      } else if (var0.length() <= 16) {
         return new BoardEntry(var0, "");
      } else {
         String var1 = var0.substring(0, 16);
         String var2 = "";
         if (var1.endsWith("§")) {
            var1 = var1.substring(0, var1.length() - 1);
            var2 = "§" + var2;
         }

         var2 = StringUtils.left(C.getLastColors(var1) + var2 + var0.substring(16), 16);
         return new BoardEntry(var1, var2);
      }
   }

   @Generated
   public String getPrefix() {
      return this.prefix;
   }

   @Generated
   public String getSuffix() {
      return this.suffix;
   }
}
