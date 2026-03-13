package com.volmit.iris.util.nbt.tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tag<T> implements Cloneable {
   public static final int DEFAULT_MAX_DEPTH = 512;
   private static final Map<String, String> ESCAPE_CHARACTERS;
   private static final Pattern ESCAPE_PATTERN = Pattern.compile("[\\\\\n\t\r\"]");
   private static final Pattern NON_QUOTE_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-+]+");
   private T value;

   public Tag(T value) {
      this.setValue(var1);
   }

   protected static String escapeString(String s, boolean lenient) {
      StringBuffer var2 = new StringBuffer();
      Matcher var3 = ESCAPE_PATTERN.matcher(var0);

      while(var3.find()) {
         var3.appendReplacement(var2, (String)ESCAPE_CHARACTERS.get(var3.group()));
      }

      var3.appendTail(var2);
      var3 = NON_QUOTE_PATTERN.matcher(var0);
      if (!var1 || !var3.matches()) {
         var2.insert(0, "\"").append("\"");
      }

      return var2.toString();
   }

   public abstract byte getID();

   public T getValue() {
      return this.value;
   }

   protected void setValue(T value) {
      this.value = this.checkValue(var1);
   }

   protected T checkValue(T value) {
      return Objects.requireNonNull(var1);
   }

   public final String toString() {
      return this.toString(512);
   }

   public String toString(int maxDepth) {
      String var10000 = this.getClass().getSimpleName();
      return "{\"type\":\"" + var10000 + "\",\"value\":" + this.valueToString(var1) + "}";
   }

   public String valueToString() {
      return this.valueToString(512);
   }

   public abstract String valueToString(int maxDepth);

   public boolean equals(Object other) {
      return var1 != null && this.getClass() == var1.getClass();
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public abstract Tag<T> clone();

   static {
      HashMap var0 = new HashMap();
      var0.put("\\", "\\\\\\\\");
      var0.put("\n", "\\\\n");
      var0.put("\t", "\\\\t");
      var0.put("\r", "\\\\r");
      var0.put("\"", "\\\\\"");
      ESCAPE_CHARACTERS = Collections.unmodifiableMap(var0);
   }
}
