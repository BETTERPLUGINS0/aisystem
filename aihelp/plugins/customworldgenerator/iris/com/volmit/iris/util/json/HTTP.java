package com.volmit.iris.util.json;

import java.util.Iterator;

public class HTTP {
   public static final String CRLF = "\r\n";

   public static JSONObject toJSONObject(String string) {
      JSONObject var1 = new JSONObject();
      HTTPTokener var2 = new HTTPTokener(var0);
      String var3 = var2.nextToken();
      if (var3.toUpperCase().startsWith("HTTP")) {
         var1.put("HTTP-Version", (Object)var3);
         var1.put("Status-Code", (Object)var2.nextToken());
         var1.put("Reason-Phrase", (Object)var2.nextTo('\u0000'));
         var2.next();
      } else {
         var1.put("Method", (Object)var3);
         var1.put("Request-URI", (Object)var2.nextToken());
         var1.put("HTTP-Version", (Object)var2.nextToken());
      }

      while(var2.more()) {
         String var4 = var2.nextTo(':');
         var2.next(':');
         var1.put(var4, (Object)var2.nextTo('\u0000'));
         var2.next();
      }

      return var1;
   }

   public static String toString(JSONObject jo) {
      Iterator var1 = var0.keys();
      StringBuilder var3 = new StringBuilder();
      if (var0.has("Status-Code") && var0.has("Reason-Phrase")) {
         var3.append(var0.getString("HTTP-Version"));
         var3.append(' ');
         var3.append(var0.getString("Status-Code"));
         var3.append(' ');
         var3.append(var0.getString("Reason-Phrase"));
      } else {
         if (!var0.has("Method") || !var0.has("Request-URI")) {
            throw new JSONException("Not enough material for an HTTP header.");
         }

         var3.append(var0.getString("Method"));
         var3.append(' ');
         var3.append('"');
         var3.append(var0.getString("Request-URI"));
         var3.append('"');
         var3.append(' ');
         var3.append(var0.getString("HTTP-Version"));
      }

      var3.append("\r\n");

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         if (!"HTTP-Version".equals(var2) && !"Status-Code".equals(var2) && !"Reason-Phrase".equals(var2) && !"Method".equals(var2) && !"Request-URI".equals(var2) && !var0.isNull(var2)) {
            var3.append(var2);
            var3.append(": ");
            var3.append(var0.getString(var2));
            var3.append("\r\n");
         }
      }

      var3.append("\r\n");
      return var3.toString();
   }
}
