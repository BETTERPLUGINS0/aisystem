package com.volmit.iris.util.json;

import com.volmit.iris.Iris;
import java.util.Iterator;

public class XML {
   public static final Character AMP = '&';
   public static final Character APOS = '\'';
   public static final Character BANG = '!';
   public static final Character EQ = '=';
   public static final Character GT = '>';
   public static final Character LT = '<';
   public static final Character QUEST = '?';
   public static final Character QUOT = '"';
   public static final Character SLASH = '/';

   public static String escape(String string) {
      StringBuilder var1 = new StringBuilder(var0.length());
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         char var4 = var0.charAt(var2);
         switch(var4) {
         case '"':
            var1.append("&quot;");
            break;
         case '&':
            var1.append("&amp;");
            break;
         case '\'':
            var1.append("&apos;");
            break;
         case '<':
            var1.append("&lt;");
            break;
         case '>':
            var1.append("&gt;");
            break;
         default:
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   public static void noSpace(String string) {
      int var2 = var0.length();
      if (var2 == 0) {
         throw new JSONException("Empty string.");
      } else {
         for(int var1 = 0; var1 < var2; ++var1) {
            if (Character.isWhitespace(var0.charAt(var1))) {
               throw new JSONException("'" + var0 + "' contains a space character.");
            }
         }

      }
   }

   private static boolean parse(XMLTokener x, JSONObject context, String name) {
      JSONObject var5 = null;
      Object var8 = var0.nextToken();
      String var6;
      if (var8 == BANG) {
         char var3 = var0.next();
         if (var3 == '-') {
            if (var0.next() == '-') {
               var0.skipPast("-->");
               return false;
            }

            var0.back();
         } else if (var3 == '[') {
            var8 = var0.nextToken();
            if ("CDATA".equals(var8) && var0.next() == '[') {
               var6 = var0.nextCDATA();
               if (var6.length() > 0) {
                  var1.accumulate("content", var6);
               }

               return false;
            }

            throw var0.syntaxError("Expected 'CDATA['");
         }

         int var4 = 1;

         do {
            var8 = var0.nextMeta();
            if (var8 == null) {
               throw var0.syntaxError("Missing '>' after '<!'.");
            }

            if (var8 == LT) {
               ++var4;
            } else if (var8 == GT) {
               --var4;
            }
         } while(var4 > 0);

         return false;
      } else if (var8 == QUEST) {
         var0.skipPast("?>");
         return false;
      } else if (var8 == SLASH) {
         var8 = var0.nextToken();
         if (var2 == null) {
            throw var0.syntaxError("Mismatched close tag " + String.valueOf(var8));
         } else if (!var8.equals(var2)) {
            throw var0.syntaxError("Mismatched " + var2 + " and " + String.valueOf(var8));
         } else if (var0.nextToken() != GT) {
            throw var0.syntaxError("Misshaped close tag");
         } else {
            return true;
         }
      } else if (var8 instanceof Character) {
         throw var0.syntaxError("Misshaped tag");
      } else {
         String var7 = (String)var8;
         var8 = null;
         var5 = new JSONObject();

         while(true) {
            if (var8 == null) {
               var8 = var0.nextToken();
            }

            if (!(var8 instanceof String)) {
               if (var8 == SLASH) {
                  if (var0.nextToken() != GT) {
                     throw var0.syntaxError("Misshaped tag");
                  }

                  if (var5.length() > 0) {
                     var1.accumulate(var7, var5);
                  } else {
                     var1.accumulate(var7, "");
                  }

                  return false;
               }

               if (var8 != GT) {
                  throw var0.syntaxError("Misshaped tag");
               }

               while(true) {
                  var8 = var0.nextContent();
                  if (var8 == null) {
                     if (var7 != null) {
                        throw var0.syntaxError("Unclosed tag " + var7);
                     }

                     return false;
                  }

                  if (var8 instanceof String) {
                     var6 = (String)var8;
                     if (var6.length() > 0) {
                        var5.accumulate("content", stringToValue(var6));
                     }
                  } else if (var8 == LT && parse(var0, var5, var7)) {
                     if (var5.length() == 0) {
                        var1.accumulate(var7, "");
                     } else if (var5.length() == 1 && var5.opt("content") != null) {
                        var1.accumulate(var7, var5.opt("content"));
                     } else {
                        var1.accumulate(var7, var5);
                     }

                     return false;
                  }
               }
            }

            var6 = (String)var8;
            var8 = var0.nextToken();
            if (var8 == EQ) {
               var8 = var0.nextToken();
               if (!(var8 instanceof String)) {
                  throw var0.syntaxError("Missing value");
               }

               var5.accumulate(var6, stringToValue((String)var8));
               var8 = null;
            } else {
               var5.accumulate(var6, "");
            }
         }
      }
   }

   public static Object stringToValue(String string) {
      if ("true".equalsIgnoreCase(var0)) {
         return Boolean.TRUE;
      } else if ("false".equalsIgnoreCase(var0)) {
         return Boolean.FALSE;
      } else if ("null".equalsIgnoreCase(var0)) {
         return JSONObject.NULL;
      } else {
         try {
            char var1 = var0.charAt(0);
            if (var1 == '-' || var1 >= '0' && var1 <= '9') {
               Long var5 = Long.valueOf(var0);
               if (var5.toString().equals(var0)) {
                  return var5;
               }
            }
         } catch (Exception var4) {
            Iris.reportError(var4);

            try {
               Double var2 = Double.valueOf(var0);
               if (var2.toString().equals(var0)) {
                  return var2;
               }
            } catch (Exception var3) {
               Iris.reportError(var3);
            }
         }

         return var0;
      }
   }

   public static JSONObject toJSONObject(String string) {
      JSONObject var1 = new JSONObject();
      XMLTokener var2 = new XMLTokener(var0);

      while(var2.more() && var2.skipPast("<")) {
         parse(var2, var1, (String)null);
      }

      return var1;
   }

   public static String toString(Object object) {
      return toString(var0, (String)null);
   }

   public static String toString(Object object, String tagName) {
      StringBuilder var2 = new StringBuilder();
      int var3;
      JSONArray var4;
      int var8;
      if (!(var0 instanceof JSONObject)) {
         if (var0.getClass().isArray()) {
            var0 = new JSONArray(var0);
         }

         if (var0 instanceof JSONArray) {
            var4 = (JSONArray)var0;
            var8 = var4.length();

            for(var3 = 0; var3 < var8; ++var3) {
               var2.append(toString(var4.opt(var3), var1 == null ? "array" : var1));
            }

            return var2.toString();
         } else {
            String var9 = var0 == null ? "null" : escape(var0.toString());
            return var1 == null ? "\"" + var9 + "\"" : (var9.length() == 0 ? "<" + var1 + "/>" : "<" + var1 + ">" + var9 + "</" + var1 + ">");
         }
      } else {
         if (var1 != null) {
            var2.append('<');
            var2.append(var1);
            var2.append('>');
         }

         JSONObject var5 = (JSONObject)var0;
         Iterator var7 = var5.keys();

         while(true) {
            while(true) {
               while(var7.hasNext()) {
                  String var6 = (String)var7.next();
                  Object var10 = var5.opt(var6);
                  if (var10 == null) {
                     var10 = "";
                  }

                  String var10000;
                  if (var10 instanceof String) {
                     var10000 = (String)var10;
                  } else {
                     var10000 = null;
                  }

                  if ("content".equals(var6)) {
                     if (var10 instanceof JSONArray) {
                        var4 = (JSONArray)var10;
                        var8 = var4.length();

                        for(var3 = 0; var3 < var8; ++var3) {
                           if (var3 > 0) {
                              var2.append('\n');
                           }

                           var2.append(escape(var4.get(var3).toString()));
                        }
                     } else {
                        var2.append(escape(var10.toString()));
                     }
                  } else if (var10 instanceof JSONArray) {
                     var4 = (JSONArray)var10;
                     var8 = var4.length();

                     for(var3 = 0; var3 < var8; ++var3) {
                        var10 = var4.get(var3);
                        if (var10 instanceof JSONArray) {
                           var2.append('<');
                           var2.append(var6);
                           var2.append('>');
                           var2.append(toString(var10));
                           var2.append("</");
                           var2.append(var6);
                           var2.append('>');
                        } else {
                           var2.append(toString(var10, var6));
                        }
                     }
                  } else if ("".equals(var10)) {
                     var2.append('<');
                     var2.append(var6);
                     var2.append("/>");
                  } else {
                     var2.append(toString(var10, var6));
                  }
               }

               if (var1 != null) {
                  var2.append("</");
                  var2.append(var1);
                  var2.append('>');
               }

               return var2.toString();
            }
         }
      }
   }
}
