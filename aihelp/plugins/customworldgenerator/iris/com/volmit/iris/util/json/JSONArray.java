package com.volmit.iris.util.json;

import com.volmit.iris.Iris;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JSONArray implements Iterable<Object> {
   private final ArrayList<Object> myArrayList;

   public JSONArray() {
      this.myArrayList = new ArrayList();
   }

   public JSONArray(JSONTokener x) {
      this();
      if (var1.nextClean() != '[') {
         throw var1.syntaxError("A JSONArray text must start with '['");
      } else if (var1.nextClean() != ']') {
         var1.back();

         while(true) {
            if (var1.nextClean() == ',') {
               var1.back();
               this.myArrayList.add(JSONObject.NULL);
            } else {
               var1.back();
               this.myArrayList.add(var1.nextValue());
            }

            switch(var1.nextClean()) {
            case ',':
               if (var1.nextClean() == ']') {
                  return;
               }

               var1.back();
               break;
            case ']':
               return;
            default:
               throw var1.syntaxError("Expected a ',' or ']'");
            }
         }
      }
   }

   public JSONArray(String source) {
      this(new JSONTokener(var1));
   }

   public JSONArray(Collection<Object> collection) {
      this.myArrayList = new ArrayList();
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.myArrayList.add(JSONObject.wrap(var2.next()));
         }
      }

   }

   public JSONArray(Object array) {
      this();
      if (!var1.getClass().isArray()) {
         throw new JSONException("JSONArray initial value should be a string or collection or array.");
      } else {
         int var2 = Array.getLength(var1);

         for(int var3 = 0; var3 < var2; ++var3) {
            this.put(JSONObject.wrap(Array.get(var1, var3)));
         }

      }
   }

   public Iterator<Object> iterator() {
      return this.myArrayList.iterator();
   }

   public Object get(int index) {
      Object var2 = this.opt(var1);
      if (var2 == null) {
         throw new JSONException("JSONArray[" + var1 + "] not found.");
      } else {
         return var2;
      }
   }

   public boolean getBoolean(int index) {
      Object var2 = this.get(var1);
      if (!var2.equals(Boolean.FALSE) && (!(var2 instanceof String) || !((String)var2).equalsIgnoreCase("false"))) {
         if (!var2.equals(Boolean.TRUE) && (!(var2 instanceof String) || !((String)var2).equalsIgnoreCase("true"))) {
            throw new JSONException("JSONArray[" + var1 + "] is not a boolean.");
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public double getDouble(int index) {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).doubleValue() : Double.parseDouble((String)var2);
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONArray[" + var1 + "] is not a number.");
      }
   }

   public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) {
      Enum var3 = this.optEnum(var1, var2);
      if (var3 == null) {
         String var10002 = JSONObject.quote(Integer.toString(var2));
         throw new JSONException("JSONObject[" + var10002 + "] is not an enum of type " + JSONObject.quote(var1.getSimpleName()) + ".");
      } else {
         return var3;
      }
   }

   public BigDecimal getBigDecimal(int index) {
      Object var2 = this.get(var1);

      try {
         return new BigDecimal(var2.toString());
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONArray[" + var1 + "] could not convert to BigDecimal.");
      }
   }

   public BigInteger getBigInteger(int index) {
      Object var2 = this.get(var1);

      try {
         return new BigInteger(var2.toString());
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONArray[" + var1 + "] could not convert to BigInteger.");
      }
   }

   public int getInt(int index) {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).intValue() : Integer.parseInt((String)var2);
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONArray[" + var1 + "] is not a number.");
      }
   }

   public JSONArray getJSONArray(int index) {
      Object var2 = this.get(var1);
      if (var2 instanceof JSONArray) {
         return (JSONArray)var2;
      } else {
         throw new JSONException("JSONArray[" + var1 + "] is not a JSONArray.");
      }
   }

   public JSONObject getJSONObject(int index) {
      Object var2 = this.get(var1);
      if (var2 instanceof JSONObject) {
         return (JSONObject)var2;
      } else {
         throw new JSONException("JSONArray[" + var1 + "] is not a JSONObject.");
      }
   }

   public long getLong(int index) {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).longValue() : Long.parseLong((String)var2);
      } catch (Exception var4) {
         Iris.reportError(var4);
         throw new JSONException("JSONArray[" + var1 + "] is not a number.");
      }
   }

   public String getString(int index) {
      Object var2 = this.get(var1);
      if (var2 instanceof String) {
         return (String)var2;
      } else {
         throw new JSONException("JSONArray[" + var1 + "] not a string.");
      }
   }

   public boolean isNull(int index) {
      return JSONObject.NULL.equals(this.opt(var1));
   }

   public String join(String separator) {
      int var2 = this.length();
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < var2; ++var4) {
         if (var4 > 0) {
            var3.append(var1);
         }

         var3.append(JSONObject.valueToString(this.myArrayList.get(var4)));
      }

      return var3.toString();
   }

   public int length() {
      return this.myArrayList.size();
   }

   public Object opt(int index) {
      return var1 >= 0 && var1 < this.length() ? this.myArrayList.get(var1) : null;
   }

   public boolean optBoolean(int index) {
      return this.optBoolean(var1, false);
   }

   public boolean optBoolean(int index, boolean defaultValue) {
      try {
         return this.getBoolean(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public double optDouble(int index) {
      return this.optDouble(var1, Double.NaN);
   }

   public double optDouble(int index, double defaultValue) {
      try {
         return this.getDouble(var1);
      } catch (Exception var5) {
         Iris.reportError(var5);
         return var2;
      }
   }

   public int optInt(int index) {
      return this.optInt(var1, 0);
   }

   public int optInt(int index, int defaultValue) {
      try {
         return this.getInt(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index) {
      return this.optEnum(var1, var2, (Enum)null);
   }

   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue) {
      try {
         Object var4 = this.opt(var2);
         if (JSONObject.NULL.equals(var4)) {
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

   public BigInteger optBigInteger(int index, BigInteger defaultValue) {
      try {
         return this.getBigInteger(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public BigDecimal optBigDecimal(int index, BigDecimal defaultValue) {
      try {
         return this.getBigDecimal(var1);
      } catch (Exception var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public JSONArray optJSONArray(int index) {
      Object var2 = this.opt(var1);
      return var2 instanceof JSONArray ? (JSONArray)var2 : null;
   }

   public JSONObject optJSONObject(int index) {
      Object var2 = this.opt(var1);
      return var2 instanceof JSONObject ? (JSONObject)var2 : null;
   }

   public long optLong(int index) {
      return this.optLong(var1, 0L);
   }

   public long optLong(int index, long defaultValue) {
      try {
         return this.getLong(var1);
      } catch (Exception var5) {
         Iris.reportError(var5);
         return var2;
      }
   }

   public String optString(int index) {
      return this.optString(var1, "");
   }

   public String optString(int index, String defaultValue) {
      Object var3 = this.opt(var1);
      return JSONObject.NULL.equals(var3) ? var2 : var3.toString();
   }

   public JSONArray put(boolean value) {
      this.put((Object)(var1 ? Boolean.TRUE : Boolean.FALSE));
      return this;
   }

   public JSONArray put(Collection<Object> value) {
      this.put((Object)(new JSONArray(var1)));
      return this;
   }

   public JSONArray put(double value) {
      Double var3 = var1;
      JSONObject.testValidity(var3);
      this.put((Object)var3);
      return this;
   }

   public JSONArray put(int value) {
      this.put((Object)var1);
      return this;
   }

   public JSONArray put(long value) {
      this.put((Object)var1);
      return this;
   }

   public JSONArray put(Map<String, Object> value) {
      this.put((Object)(new JSONObject(var1)));
      return this;
   }

   public JSONArray put(Object value) {
      this.myArrayList.add(var1);
      return this;
   }

   public JSONArray put(int index, boolean value) {
      this.put(var1, (Object)(var2 ? Boolean.TRUE : Boolean.FALSE));
      return this;
   }

   public JSONArray put(int index, Collection<Object> value) {
      this.put(var1, (Object)(new JSONArray(var2)));
      return this;
   }

   public JSONArray put(int index, double value) {
      this.put(var1, (Object)var2);
      return this;
   }

   public JSONArray put(int index, int value) {
      this.put(var1, (Object)var2);
      return this;
   }

   public JSONArray put(int index, long value) {
      this.put(var1, (Object)var2);
      return this;
   }

   public JSONArray put(int index, Map<String, Object> value) {
      this.put(var1, (Object)(new JSONObject(var2)));
      return this;
   }

   public JSONArray put(int index, Object value) {
      JSONObject.testValidity(var2);
      if (var1 < 0) {
         throw new JSONException("JSONArray[" + var1 + "] not found.");
      } else {
         if (var1 < this.length()) {
            this.myArrayList.set(var1, var2);
         } else {
            while(var1 != this.length()) {
               this.put(JSONObject.NULL);
            }

            this.put(var2);
         }

         return this;
      }
   }

   public Object remove(int index) {
      return var1 >= 0 && var1 < this.length() ? this.myArrayList.remove(var1) : null;
   }

   public boolean similar(Object other) {
      if (!(var1 instanceof JSONArray)) {
         return false;
      } else {
         int var2 = this.length();
         if (var2 != ((JSONArray)var1).length()) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               Object var4 = this.get(var3);
               Object var5 = ((JSONArray)var1).get(var3);
               if (var4 instanceof JSONObject) {
                  if (!((JSONObject)var4).similar(var5)) {
                     return false;
                  }
               } else if (var4 instanceof JSONArray) {
                  if (!((JSONArray)var4).similar(var5)) {
                     return false;
                  }
               } else if (!var4.equals(var5)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public JSONObject toJSONObject(JSONArray names) {
      if (var1 != null && var1.length() != 0 && this.length() != 0) {
         JSONObject var2 = new JSONObject();

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            var2.put(var1.getString(var3), this.opt(var3));
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
         var1.write(91);
         if (var5 == 1) {
            JSONObject.writeValue(var1, this.myArrayList.get(0), var2, var3);
         } else if (var5 != 0) {
            int var6 = var3 + var2;

            for(int var7 = 0; var7 < var5; ++var7) {
               if (var4) {
                  var1.write(44);
               }

               if (var2 > 0) {
                  var1.write(10);
               }

               JSONObject.indent(var1, var6);
               JSONObject.writeValue(var1, this.myArrayList.get(var7), var2, var6);
               var4 = true;
            }

            if (var2 > 0) {
               var1.write(10);
            }

            JSONObject.indent(var1, var3);
         }

         var1.write(93);
         return var1;
      } catch (IOException var8) {
         throw new JSONException(var8);
      }
   }
}
