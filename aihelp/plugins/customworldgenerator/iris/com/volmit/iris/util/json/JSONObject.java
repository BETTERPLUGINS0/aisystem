package com.volmit.iris.util.json;

import com.volmit.iris.Iris;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

public class JSONObject {
   public static final Object NULL = new JSONObject.Null();
   private final LinkedHashMap<String, Object> map;

   public JSONObject() {
      this.map = new LinkedHashMap();
   }

   public JSONObject(JSONObject jo, String[] names) {
      this();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         try {
            this.putOnce(var2[var3], var1.opt(var2[var3]));
         } catch (Exception var5) {
            Iris.reportError(var5);
         }
      }

   }

   public JSONObject(JSONTokener x) {
      this();
      if (var1.nextClean() != '{') {
         throw var1.syntaxError("A JSONObject text must begin with '{'");
      } else {
         while(true) {
            char var2 = var1.nextClean();
            switch(var2) {
            case '\u0000':
               throw var1.syntaxError("A JSONObject text must end with '}'");
            case '}':
               return;
            default:
               var1.back();
               String var3 = var1.nextValue().toString();
               var2 = var1.nextClean();
               if (var2 != ':') {
                  throw var1.syntaxError("Expected a ':' after a key");
               }

               this.putOnce(var3, var1.nextValue());
               switch(var1.nextClean()) {
               case ',':
               case ';':
                  if (var1.nextClean() == '}') {
                     return;
                  }

                  var1.back();
                  break;
               case '}':
                  return;
               default:
                  throw var1.syntaxError("Expected a ',' or '}'");
               }
            }
         }
      }
   }

   public JSONObject(Map<String, Object> map) {
      this.map = new LinkedHashMap();
      if (var1 != null) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Object var4 = var3.getValue();
            if (var4 != null) {
               this.map.put((String)var3.getKey(), wrap(var4));
            }
         }
      }

   }

   public JSONObject(Object bean) {
      this();
      this.populateMap(var1);
   }

   public JSONObject(Object object, String[] names) {
      this();
      Class var3 = var1.getClass();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];

         try {
            this.putOpt(var5, var3.getField(var5).get(var1));
         } catch (Exception var7) {
            Iris.reportError(var7);
         }
      }

   }

   public JSONObject(String source) {
      this(new JSONTokener(var1));
   }

   public JSONObject(String baseName, Locale locale) {
      this();
      ResourceBundle var3 = ResourceBundle.getBundle(var1, var2, Thread.currentThread().getContextClassLoader());
      Enumeration var4 = var3.getKeys();

      while(true) {
         Object var5;
         do {
            if (!var4.hasMoreElements()) {
               return;
            }

            var5 = var4.nextElement();
         } while(var5 == null);

         String[] var6 = ((String)var5).split("\\.");
         int var7 = var6.length - 1;
         JSONObject var8 = this;

         for(int var9 = 0; var9 < var7; ++var9) {
            String var10 = var6[var9];
            JSONObject var11 = var8.optJSONObject(var10);
            if (var11 == null) {
               var11 = new JSONObject();
               var8.put(var10, (Object)var11);
            }

            var8 = var11;
         }

         var8.put(var6[var7], (Object)var3.getString((String)var5));
      }
   }

   public static String doubleToString(double d) {
      if (!Double.isInfinite(var0) && !Double.isNaN(var0)) {
         String var2 = Double.toString(var0);
         if (var2.indexOf(46) > 0 && var2.indexOf(101) < 0 && var2.indexOf(69) < 0) {
            while(var2.endsWith("0")) {
               var2 = var2.substring(0, var2.length() - 1);
            }

            if (var2.endsWith(".")) {
               var2 = var2.substring(0, var2.length() - 1);
            }
         }

         return var2;
      } else {
         return "null";
      }
   }

   public static String[] getNames(JSONObject jo) {
      int var1 = var0.length();
      if (var1 == 0) {
         return null;
      } else {
         Iterator var2 = var0.keys();
         String[] var3 = new String[var1];

         for(int var4 = 0; var2.hasNext(); ++var4) {
            var3[var4] = (String)var2.next();
         }

         return var3;
      }
   }

   public static String[] getNames(Object object) {
      if (var0 == null) {
         return null;
      } else {
         Class var1 = var0.getClass();
         Field[] var2 = var1.getFields();
         int var3 = var2.length;
         if (var3 == 0) {
            return null;
         } else {
            String[] var4 = new String[var3];

            for(int var5 = 0; var5 < var3; ++var5) {
               var4[var5] = var2[var5].getName();
            }

            return var4;
         }
      }
   }

   public static String numberToString(Number number) {
      if (var0 == null) {
         throw new JSONException("Null pointer");
      } else {
         testValidity(var0);
         String var1 = var0.toString();
         if (var1.indexOf(46) > 0 && var1.indexOf(101) < 0 && var1.indexOf(69) < 0) {
            while(var1.endsWith("0")) {
               var1 = var1.substring(0, var1.length() - 1);
            }

            if (var1.endsWith(".")) {
               var1 = var1.substring(0, var1.length() - 1);
            }
         }

         return var1;
      }
   }

   public static String quote(String string) {
      StringWriter var1 = new StringWriter();
      synchronized(var1.getBuffer()) {
         String var10000;
         try {
            var10000 = quote(var0, var1).toString();
         } catch (IOException var5) {
            Iris.reportError(var5);
            return "";
         }

         return var10000;
      }
   }

   public static Writer quote(String string, Writer w) {
      if (var0 != null && var0.length() != 0) {
         char var3 = 0;
         int var6 = var0.length();
         var1.write(34);

         for(int var5 = 0; var5 < var6; ++var5) {
            char var2 = var3;
            var3 = var0.charAt(var5);
            switch(var3) {
            case '\b':
               var1.write("\\b");
               continue;
            case '\t':
               var1.write("\\t");
               continue;
            case '\n':
               var1.write("\\n");
               continue;
            case '\f':
               var1.write("\\f");
               continue;
            case '\r':
               var1.write("\\r");
               continue;
            case '"':
            case '\\':
               var1.write(92);
               var1.write(var3);
               continue;
            case '/':
               if (var2 == '<') {
                  var1.write(92);
               }

               var1.write(var3);
               continue;
            }

            if (var3 >= ' ' && (var3 < 128 || var3 >= 160) && (var3 < 8192 || var3 >= 8448)) {
               var1.write(var3);
            } else {
               var1.write("\\u");
               String var4 = Integer.toHexString(var3);
               var1.write("0000", 0, 4 - var4.length());
               var1.write(var4);
            }
         }

         var1.write(34);
         return var1;
      } else {
         var1.write("\"\"");
         return var1;
      }
   }

   public static Object stringToValue(String string) {
      if (var0.equals("")) {
         return var0;
      } else if (var0.equalsIgnoreCase("true")) {
         return Boolean.TRUE;
      } else if (var0.equalsIgnoreCase("false")) {
         return Boolean.FALSE;
      } else if (var0.equalsIgnoreCase("null")) {
         return NULL;
      } else {
         char var2 = var0.charAt(0);
         if (var2 >= '0' && var2 <= '9' || var2 == '-') {
            try {
               if (var0.indexOf(46) <= -1 && var0.indexOf(101) <= -1 && var0.indexOf(69) <= -1) {
                  Long var3 = Long.valueOf(var0);
                  if (var0.equals(var3.toString())) {
                     if (var3 == (long)var3.intValue()) {
                        return var3.intValue();
                     }

                     return var3;
                  }
               } else {
                  Double var1 = Double.valueOf(var0);
                  if (!var1.isInfinite() && !var1.isNaN()) {
                     return var1;
                  }
               }
            } catch (Exception var4) {
               Iris.reportError(var4);
            }
         }

         return var0;
      }
   }

   public static void testValidity(Object o) {
      if (var0 != null) {
         if (var0 instanceof Double) {
            if (((Double)var0).isInfinite() || ((Double)var0).isNaN()) {
               throw new JSONException("JSON does not allow non-finite numbers.");
            }
         } else if (var0 instanceof Float && (((Float)var0).isInfinite() || ((Float)var0).isNaN())) {
            throw new JSONException("JSON does not allow non-finite numbers.");
         }
      }

   }

   public static String valueToString(Object value) {
      if (var0 != null && !var0.equals((Object)null)) {
         if (var0 instanceof JSONString) {
            String var5;
            try {
               var5 = ((JSONString)var0).toJSONString();
            } catch (Exception var3) {
               Iris.reportError(var3);
               throw new JSONException(var3);
            }

            if (var5 instanceof String) {
               return (String)var5;
            } else {
               throw new JSONException("Bad value from toJSONString: " + String.valueOf(var5));
            }
         } else if (var0 instanceof Number) {
            return numberToString((Number)var0);
         } else if (!(var0 instanceof Boolean) && !(var0 instanceof JSONObject) && !(var0 instanceof JSONArray)) {
            if (var0 instanceof Map) {
               Map var4 = (Map)var0;
               return (new JSONObject(var4)).toString();
            } else if (var0 instanceof Collection) {
               Collection var1 = (Collection)var0;
               return (new JSONArray(var1)).toString();
            } else {
               return var0.getClass().isArray() ? (new JSONArray(var0)).toString() : quote(var0.toString());
            }
         } else {
            return var0.toString();
         }
      } else {
         return "null";
      }
   }

   public static Object wrap(Object object) {
      try {
         if (var0 == null) {
            return NULL;
         } else if (!(var0 instanceof JSONObject) && !(var0 instanceof JSONArray) && !NULL.equals(var0) && !(var0 instanceof JSONString) && !(var0 instanceof Byte) && !(var0 instanceof Character) && !(var0 instanceof Short) && !(var0 instanceof Integer) && !(var0 instanceof Long) && !(var0 instanceof Boolean) && !(var0 instanceof Float) && !(var0 instanceof Double) && !(var0 instanceof String) && !(var0 instanceof BigInteger) && !(var0 instanceof BigDecimal)) {
            if (var0 instanceof Collection) {
               Collection var5 = (Collection)var0;
               return new JSONArray(var5);
            } else if (var0.getClass().isArray()) {
               return new JSONArray(var0);
            } else if (var0 instanceof Map) {
               Map var4 = (Map)var0;
               return new JSONObject(var4);
            } else {
               Package var1 = var0.getClass().getPackage();
               String var2 = var1 != null ? var1.getName() : "";
               return !var2.startsWith("java.") && !var2.startsWith("javax.") && var0.getClass().getClassLoader() != null ? new JSONObject(var0) : var0.toString();
            }
         } else {
            return var0;
         }
      } catch (Exception var3) {
         Iris.reportError(var3);
         return null;
      }
   }

   static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) {
      if (var1 != null && !var1.equals((Object)null)) {
         if (var1 instanceof JSONObject) {
            ((JSONObject)var1).write(var0, var2, var3);
         } else if (var1 instanceof JSONArray) {
            ((JSONArray)var1).write(var0, var2, var3);
         } else if (var1 instanceof Map) {
            Map var4 = (Map)var1;
            (new JSONObject(var4)).write(var0, var2, var3);
         } else if (var1 instanceof Collection) {
            Collection var7 = (Collection)var1;
            (new JSONArray(var7)).write(var0, var2, var3);
         } else if (var1.getClass().isArray()) {
            (new JSONArray(var1)).write(var0, var2, var3);
         } else if (var1 instanceof Number) {
            var0.write(numberToString((Number)var1));
         } else if (var1 instanceof Boolean) {
            var0.write(var1.toString());
         } else if (var1 instanceof JSONString) {
            String var8;
            try {
               var8 = ((JSONString)var1).toJSONString();
            } catch (Exception var6) {
               Iris.reportError(var6);
               throw new JSONException(var6);
            }

            var0.write(var8 != null ? var8.toString() : quote(var1.toString()));
         } else {
            quote(var1.toString(), var0);
         }
      } else {
         var0.write("null");
      }

      return var0;
   }

   static final void indent(Writer writer, int indent) {
      for(int var2 = 0; var2 < var1; ++var2) {
         var0.write(32);
      }

   }

   public JSONObject accumulate(String key, Object value) {
      testValidity(var2);
      Object var3 = this.opt(var1);
      if (var3 == null) {
         this.put(var1, var2 instanceof JSONArray ? (new JSONArray()).put(var2) : var2);
      } else if (var3 instanceof JSONArray) {
         ((JSONArray)var3).put(var2);
      } else {
         this.put(var1, (Object)(new JSONArray()).put(var3).put(var2));
      }

      return this;
   }

   public JSONObject append(String key, Object value) {
      testValidity(var2);
      Object var3 = this.opt(var1);
      if (var3 == null) {
         this.put(var1, (Object)(new JSONArray()).put(var2));
      } else {
         if (!(var3 instanceof JSONArray)) {
            throw new JSONException("JSONObject[" + var1 + "] is not a JSONArray.");
         }

         this.put(var1, (Object)((JSONArray)var3).put(var2));
      }

      return this;
   }

   public Object get(String key) {
      if (var1 == null) {
         throw new JSONException("Null key.");
      } else {
         Object var2 = this.opt(var1);
         if (var2 == null) {
            throw new JSONException("JSONObject[" + quote(var1) + "] not found.");
         } else {
            return var2;
         }
      }
   }

   public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
      Enum var3 = this.optEnum(var1, var2);
      if (var3 == null) {
         String var10002 = quote(var2);
         throw new JSONException("JSONObject[" + var10002 + "] is not an enum of type " + quote(var1.getSimpleName()) + ".");
      } else {
         return var3;
      }
   }

   public boolean getBoolean(String key) {
      Object var2 = this.get(var1);
      if (!var2.equals(Boolean.FALSE) && (!(var2 instanceof String) || !((String)var2).equalsIgnoreCase("false"))) {
         if (!var2.equals(Boolean.TRUE) && (!(var2 instanceof String) || !((String)var2).equalsIgnoreCase("true"))) {
            throw new JSONException("JSONObject[" + quote(var1) + "] is not a Boolean.");
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public BigInteger getBigInteger(String key) {
      Object var2 = this.get(var1);

      try {
         return new BigInteger(var2.toString());
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONObject[" + quote(var1) + "] could not be converted to BigInteger.");
      }
   }

   public BigDecimal getBigDecimal(String key) {
      Object var2 = this.get(var1);

      try {
         return new BigDecimal(var2.toString());
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONObject[" + quote(var1) + "] could not be converted to BigDecimal.");
      }
   }

   public double getDouble(String key) {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).doubleValue() : Double.parseDouble((String)var2);
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONObject[" + quote(var1) + "] is not a number.");
      }
   }

   public int getInt(String key) {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).intValue() : Integer.parseInt((String)var2);
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONObject[" + quote(var1) + "] is not an int.");
      }
   }

   public JSONArray getJSONArray(String key) {
      Object var2 = this.get(var1);
      if (var2 instanceof JSONArray) {
         return (JSONArray)var2;
      } else {
         throw new JSONException("JSONObject[" + quote(var1) + "] is not a JSONArray.");
      }
   }

   public JSONObject getJSONObject(String key) {
      Object var2 = this.get(var1);
      if (var2 instanceof JSONObject) {
         return (JSONObject)var2;
      } else {
         throw new JSONException("JSONObject[" + quote(var1) + "] is not a JSONObject.");
      }
   }

   public long getLong(String key) {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).longValue() : Long.parseLong((String)var2);
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONObject[" + quote(var1) + "] is not a long.");
      }
   }

   public String getString(String key) {
      Object var2 = this.get(var1);
      if (var2 instanceof String) {
         return (String)var2;
      } else {
         throw new JSONException("JSONObject[" + quote(var1) + "] not a string.");
      }
   }

   public boolean has(String key) {
      return this.map.containsKey(var1);
   }

   public JSONObject increment(String key) {
      Object var2 = this.opt(var1);
      if (var2 == null) {
         this.put(var1, 1);
      } else if (var2 instanceof BigInteger) {
         this.put(var1, (Object)((BigInteger)var2).add(BigInteger.ONE));
      } else if (var2 instanceof BigDecimal) {
         this.put(var1, (Object)((BigDecimal)var2).add(BigDecimal.ONE));
      } else if (var2 instanceof Integer) {
         this.put(var1, (Integer)var2 + 1);
      } else if (var2 instanceof Long) {
         this.put(var1, (Long)var2 + 1L);
      } else if (var2 instanceof Double) {
         this.put(var1, (Double)var2 + 1.0D);
      } else {
         if (!(var2 instanceof Float)) {
            throw new JSONException("Unable to increment [" + quote(var1) + "].");
         }

         this.put(var1, (double)((Float)var2 + 1.0F));
      }

      return this;
   }

   public boolean isNull(String key) {
      return NULL.equals(this.opt(var1));
   }

   public Iterator<String> keys() {
      return this.keySet().iterator();
   }

   public Set<String> keySet() {
      return this.map.keySet();
   }

   public int length() {
      return this.map.size();
   }

   public JSONArray names() {
      JSONArray var1 = new JSONArray();
      Iterator var2 = this.keys();

      while(var2.hasNext()) {
         var1.put(var2.next());
      }

      return var1.length() == 0 ? null : var1;
   }

   public Object opt(String key) {
      return var1 == null ? null : this.map.get(var1);
   }

   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key) {
      return this.optEnum(var1, var2, (Enum)null);
   }

   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue) {
      try {
         Object var4 = this.opt(var2);
         if (NULL.equals(var4)) {
            return var3;
         } else if (var1.isAssignableFrom(var4.getClass())) {
            Enum var5 = (Enum)var4;
            return var5;
         } else {
            return Enum.valueOf(var1, var4.toString());
         }
      } catch (IllegalArgumentException var6) {
         Iris.reportError(var6);
         return var3;
      } catch (NullPointerException var7) {
         Iris.reportError(var7);
         return var3;
      }
   }

   public boolean optBoolean(String key) {
      return this.optBoolean(var1, false);
   }

   public boolean optBoolean(String key, boolean defaultValue) {
      try {
         return this.getBoolean(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public double optDouble(String key) {
      return this.optDouble(var1, Double.NaN);
   }

   public BigInteger optBigInteger(String key, BigInteger defaultValue) {
      try {
         return this.getBigInteger(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public BigDecimal optBigDecimal(String key, BigDecimal defaultValue) {
      try {
         return this.getBigDecimal(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public double optDouble(String key, double defaultValue) {
      try {
         return this.getDouble(var1);
      } catch (Exception var5) {
         Iris.reportError(var5);
         return var2;
      }
   }

   public int optInt(String key) {
      return this.optInt(var1, 0);
   }

   public int optInt(String key, int defaultValue) {
      try {
         return this.getInt(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public JSONArray optJSONArray(String key) {
      Object var2 = this.opt(var1);
      return var2 instanceof JSONArray ? (JSONArray)var2 : null;
   }

   public JSONObject optJSONObject(String key) {
      Object var2 = this.opt(var1);
      return var2 instanceof JSONObject ? (JSONObject)var2 : null;
   }

   public long optLong(String key) {
      return this.optLong(var1, 0L);
   }

   public long optLong(String key, long defaultValue) {
      try {
         return this.getLong(var1);
      } catch (Exception var5) {
         Iris.reportError(var5);
         return var2;
      }
   }

   public String optString(String key) {
      return this.optString(var1, "");
   }

   public String optString(String key, String defaultValue) {
      Object var3 = this.opt(var1);
      return NULL.equals(var3) ? var2 : var3.toString();
   }

   private void populateMap(Object bean) {
      Class var2 = var1.getClass();
      boolean var3 = var2.getClassLoader() != null;
      Method[] var4 = var3 ? var2.getMethods() : var2.getDeclaredMethods();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         try {
            Method var6 = var4[var5];
            if (Modifier.isPublic(var6.getModifiers())) {
               String var7 = var6.getName();
               String var8 = "";
               if (var7.startsWith("get")) {
                  if (!"getClass".equals(var7) && !"getDeclaringClass".equals(var7)) {
                     var8 = var7.substring(3);
                  } else {
                     var8 = "";
                  }
               } else if (var7.startsWith("is")) {
                  var8 = var7.substring(2);
               }

               if (var8.length() > 0 && Character.isUpperCase(var8.charAt(0)) && var6.getParameterTypes().length == 0) {
                  if (var8.length() == 1) {
                     var8 = var8.toLowerCase();
                  } else if (!Character.isUpperCase(var8.charAt(1))) {
                     String var10000 = var8.substring(0, 1).toLowerCase();
                     var8 = var10000 + var8.substring(1);
                  }

                  Object var9 = var6.invoke(var1, (Object[])null);
                  if (var9 != null) {
                     this.map.put(var8, wrap(var9));
                  }
               }
            }
         } catch (Exception var10) {
            Iris.reportError(var10);
         }
      }

   }

   public JSONObject put(String key, boolean value) {
      this.put(var1, (Object)(var2 ? Boolean.TRUE : Boolean.FALSE));
      return this;
   }

   public JSONObject put(String key, Collection<Object> value) {
      this.put(var1, (Object)(new JSONArray(var2)));
      return this;
   }

   public JSONObject put(String key, double value) {
      this.put(var1, (Object)var2);
      return this;
   }

   public JSONObject put(String key, int value) {
      this.put(var1, (Object)var2);
      return this;
   }

   public JSONObject put(String key, long value) {
      this.put(var1, (Object)var2);
      return this;
   }

   public JSONObject put(String key, Map<String, Object> value) {
      this.put(var1, (Object)(new JSONObject(var2)));
      return this;
   }

   public JSONObject put(String key, Object value) {
      if (var1 == null) {
         throw new NullPointerException("Null key.");
      } else {
         if (var2 != null) {
            testValidity(var2);
            this.map.put(var1, var2);
         } else {
            this.remove(var1);
         }

         return this;
      }
   }

   public JSONObject putOnce(String key, Object value) {
      if (var1 != null && var2 != null) {
         if (this.opt(var1) != null) {
            throw new JSONException("Duplicate key \"" + var1 + "\"");
         }

         this.put(var1, var2);
      }

      return this;
   }

   public JSONObject putOpt(String key, Object value) {
      if (var1 != null && var2 != null) {
         this.put(var1, var2);
      }

      return this;
   }

   public Object remove(String key) {
      return this.map.remove(var1);
   }

   public boolean similar(Object other) {
      try {
         if (!(var1 instanceof JSONObject)) {
            return false;
         } else {
            Set var2 = this.keySet();
            if (!var2.equals(((JSONObject)var1).keySet())) {
               return false;
            } else {
               Iterator var3 = var2.iterator();

               while(var3.hasNext()) {
                  String var4 = (String)var3.next();
                  Object var5 = this.get(var4);
                  Object var6 = ((JSONObject)var1).get(var4);
                  if (var5 instanceof JSONObject) {
                     if (!((JSONObject)var5).similar(var6)) {
                        return false;
                     }
                  } else if (var5 instanceof JSONArray) {
                     if (!((JSONArray)var5).similar(var6)) {
                        return false;
                     }
                  } else if (!var5.equals(var6)) {
                     return false;
                  }
               }

               return true;
            }
         }
      } catch (Throwable var7) {
         Iris.reportError(var7);
         return false;
      }
   }

   public JSONArray toJSONArray(JSONArray names) {
      if (var1 != null && var1.length() != 0) {
         JSONArray var2 = new JSONArray();

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            var2.put(this.opt(var1.getString(var3)));
         }

         return var2;
      } else {
         return null;
      }
   }

   public String toString() {
      try {
         return this.toString(0);
      } catch (Exception var2) {
         Iris.reportError(var2);
         return null;
      }
   }

   public String toString(int indentFactor) {
      StringWriter var2 = new StringWriter();
      synchronized(var2.getBuffer()) {
         return this.write(var2, var1, 0).toString();
      }
   }

   public Writer write(Writer writer) {
      return this.write(var1, 0, 0);
   }

   Writer write(Writer writer, int indentFactor, int indent) {
      try {
         boolean var4 = false;
         int var5 = this.length();
         Iterator var6 = this.keys();
         var1.write(123);
         if (var5 == 1) {
            Object var7 = var6.next();
            var1.write(quote(var7.toString()));
            var1.write(58);
            if (var2 > 0) {
               var1.write(32);
            }

            writeValue(var1, this.map.get(var7), var2, var3);
         } else if (var5 != 0) {
            for(int var10 = var3 + var2; var6.hasNext(); var4 = true) {
               Object var8 = var6.next();
               if (var4) {
                  var1.write(44);
               }

               if (var2 > 0) {
                  var1.write(10);
               }

               indent(var1, var10);
               var1.write(quote(var8.toString()));
               var1.write(58);
               if (var2 > 0) {
                  var1.write(32);
               }

               writeValue(var1, this.map.get(var8), var2, var10);
            }

            if (var2 > 0) {
               var1.write(10);
            }

            indent(var1, var3);
         }

         var1.write(125);
         return var1;
      } catch (IOException var9) {
         Iris.reportError(var9);
         throw new JSONException(var9);
      }
   }

   private static final class Null {
      protected final Object clone() {
         return this;
      }

      public boolean equals(Object object) {
         return var1 == null || var1 == this;
      }

      public String toString() {
         return "null";
      }
   }
}
