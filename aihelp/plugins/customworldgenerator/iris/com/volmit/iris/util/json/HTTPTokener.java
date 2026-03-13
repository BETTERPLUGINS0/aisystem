package com.volmit.iris.util.json;

public class HTTPTokener extends JSONTokener {
   public HTTPTokener(String string) {
      super(var1);
   }

   public String nextToken() {
      StringBuilder var3 = new StringBuilder();

      char var1;
      do {
         var1 = this.next();
      } while(Character.isWhitespace(var1));

      if (var1 != '"' && var1 != '\'') {
         while(var1 != 0 && !Character.isWhitespace(var1)) {
            var3.append(var1);
            var1 = this.next();
         }

         return var3.toString();
      } else {
         char var2 = var1;

         while(true) {
            var1 = this.next();
            if (var1 < ' ') {
               throw this.syntaxError("Unterminated string.");
            }

            if (var1 == var2) {
               return var3.toString();
            }

            var3.append(var1);
         }
      }
   }
}
