package com.volmit.iris.util.json;

import java.util.Iterator;

public class JSONML {
   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja) {
      String var5 = null;
      JSONArray var7 = null;
      JSONObject var8 = null;
      String var10 = null;

      while(true) {
         while(var0.more()) {
            Object var9 = var0.nextContent();
            if (var9 == XML.LT) {
               var9 = var0.nextToken();
               if (var9 instanceof Character) {
                  if (var9 == XML.SLASH) {
                     var9 = var0.nextToken();
                     if (!(var9 instanceof String)) {
                        throw new JSONException("Expected a closing name instead of '" + String.valueOf(var9) + "'.");
                     }

                     if (var0.nextToken() != XML.GT) {
                        throw var0.syntaxError("Misshaped close tag");
                     }

                     return var9;
                  }

                  if (var9 != XML.BANG) {
                     if (var9 != XML.QUEST) {
                        throw var0.syntaxError("Misshaped tag");
                     }

                     var0.skipPast("?>");
                  } else {
                     char var4 = var0.next();
                     if (var4 == '-') {
                        if (var0.next() == '-') {
                           var0.skipPast("-->");
                        } else {
                           var0.back();
                        }
                     } else if (var4 == '[') {
                        var9 = var0.nextToken();
                        if (!var9.equals("CDATA") || var0.next() != '[') {
                           throw var0.syntaxError("Expected 'CDATA['");
                        }

                        if (var2 != null) {
                           var2.put((Object)var0.nextCDATA());
                        }
                     } else {
                        int var6 = 1;

                        while(true) {
                           var9 = var0.nextMeta();
                           if (var9 == null) {
                              throw var0.syntaxError("Missing '>' after '<!'.");
                           }

                           if (var9 == XML.LT) {
                              ++var6;
                           } else if (var9 == XML.GT) {
                              --var6;
                           }

                           if (var6 <= 0) {
                              break;
                           }
                        }
                     }
                  }
               } else {
                  if (!(var9 instanceof String)) {
                     throw var0.syntaxError("Bad tagName '" + String.valueOf(var9) + "'.");
                  }

                  var10 = (String)var9;
                  var7 = new JSONArray();
                  var8 = new JSONObject();
                  if (var1) {
                     var7.put((Object)var10);
                     if (var2 != null) {
                        var2.put((Object)var7);
                     }
                  } else {
                     var8.put("tagName", (Object)var10);
                     if (var2 != null) {
                        var2.put((Object)var8);
                     }
                  }

                  var9 = null;

                  while(true) {
                     if (var9 == null) {
                        var9 = var0.nextToken();
                     }

                     if (var9 == null) {
                        throw var0.syntaxError("Misshaped tag");
                     }

                     if (!(var9 instanceof String)) {
                        if (var1 && var8.length() > 0) {
                           var7.put((Object)var8);
                        }

                        if (var9 == XML.SLASH) {
                           if (var0.nextToken() != XML.GT) {
                              throw var0.syntaxError("Misshaped tag");
                           }

                           if (var2 == null) {
                              if (var1) {
                                 return var7;
                              }

                              return var8;
                           }
                        } else {
                           if (var9 != XML.GT) {
                              throw var0.syntaxError("Misshaped tag");
                           }

                           var5 = (String)parse(var0, var1, var7);
                           if (var5 != null) {
                              if (!var5.equals(var10)) {
                                 throw var0.syntaxError("Mismatched '" + var10 + "' and '" + var5 + "'");
                              }

                              var10 = null;
                              if (!var1 && var7.length() > 0) {
                                 var8.put("childNodes", (Object)var7);
                              }

                              if (var2 == null) {
                                 if (var1) {
                                    return var7;
                                 }

                                 return var8;
                              }
                           }
                        }
                        break;
                     }

                     String var3 = (String)var9;
                     if (!var1 && ("tagName".equals(var3) || "childNode".equals(var3))) {
                        throw var0.syntaxError("Reserved attribute.");
                     }

                     var9 = var0.nextToken();
                     if (var9 == XML.EQ) {
                        var9 = var0.nextToken();
                        if (!(var9 instanceof String)) {
                           throw var0.syntaxError("Missing value");
                        }

                        var8.accumulate(var3, XML.stringToValue((String)var9));
                        var9 = null;
                     } else {
                        var8.accumulate(var3, "");
                     }
                  }
               }
            } else if (var2 != null) {
               var2.put(var9 instanceof String ? XML.stringToValue((String)var9) : var9);
            }
         }

         throw var0.syntaxError("Bad XML");
      }
   }

   public static JSONArray toJSONArray(String string) {
      return toJSONArray(new XMLTokener(var0));
   }

   public static JSONArray toJSONArray(XMLTokener x) {
      return (JSONArray)parse(var0, true, (JSONArray)null);
   }

   public static JSONObject toJSONObject(XMLTokener x) {
      return (JSONObject)parse(var0, false, (JSONArray)null);
   }

   public static JSONObject toJSONObject(String string) {
      return toJSONObject(new XMLTokener(var0));
   }

   public static String toString(JSONArray ja) {
      StringBuilder var7 = new StringBuilder();
      String var8 = var0.getString(0);
      XML.noSpace(var8);
      var8 = XML.escape(var8);
      var7.append('<');
      var7.append(var8);
      Object var6 = var0.opt(1);
      int var1;
      if (var6 instanceof JSONObject) {
         var1 = 2;
         JSONObject var2 = (JSONObject)var6;
         Iterator var4 = var2.keys();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            XML.noSpace(var3);
            String var9 = var2.optString(var3);
            if (var9 != null) {
               var7.append(' ');
               var7.append(XML.escape(var3));
               var7.append('=');
               var7.append('"');
               var7.append(XML.escape(var9));
               var7.append('"');
            }
         }
      } else {
         var1 = 1;
      }

      int var5 = var0.length();
      if (var1 >= var5) {
         var7.append('/');
      } else {
         var7.append('>');

         do {
            var6 = var0.get(var1);
            ++var1;
            if (var6 != null) {
               if (var6 instanceof String) {
                  var7.append(XML.escape(var6.toString()));
               } else if (var6 instanceof JSONObject) {
                  var7.append(toString((JSONObject)var6));
               } else if (var6 instanceof JSONArray) {
                  var7.append(toString((JSONArray)var6));
               } else {
                  var7.append(var6);
               }
            }
         } while(var1 < var5);

         var7.append('<');
         var7.append('/');
         var7.append(var8);
      }

      var7.append('>');
      return var7.toString();
   }

   public static String toString(JSONObject jo) {
      StringBuilder var1 = new StringBuilder();
      String var8 = var0.optString("tagName");
      if (var8 == null) {
         return XML.escape(var0.toString());
      } else {
         XML.noSpace(var8);
         var8 = XML.escape(var8);
         var1.append('<');
         var1.append(var8);
         Iterator var5 = var0.keys();

         while(var5.hasNext()) {
            String var4 = (String)var5.next();
            if (!"tagName".equals(var4) && !"childNodes".equals(var4)) {
               XML.noSpace(var4);
               String var9 = var0.optString(var4);
               if (var9 != null) {
                  var1.append(' ');
                  var1.append(XML.escape(var4));
                  var1.append('=');
                  var1.append('"');
                  var1.append(XML.escape(var9));
                  var1.append('"');
               }
            }
         }

         JSONArray var3 = var0.optJSONArray("childNodes");
         if (var3 == null) {
            var1.append('/');
         } else {
            var1.append('>');
            int var6 = var3.length();

            for(int var2 = 0; var2 < var6; ++var2) {
               Object var7 = var3.get(var2);
               if (var7 != null) {
                  if (var7 instanceof String) {
                     var1.append(XML.escape(var7.toString()));
                  } else if (var7 instanceof JSONObject) {
                     var1.append(toString((JSONObject)var7));
                  } else if (var7 instanceof JSONArray) {
                     var1.append(toString((JSONArray)var7));
                  } else {
                     var1.append(var7);
                  }
               }
            }

            var1.append('<');
            var1.append('/');
            var1.append(var8);
         }

         var1.append('>');
         return var1.toString();
      }
   }
}
