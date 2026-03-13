package com.volmit.iris.core.nms.container;

import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class BlockProperty {
   private static final Set<Class<?>> NATIVES = Set.of(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, String.class);
   private final String name;
   private final Class<?> type;
   private final Object defaultValue;
   private final Set<Object> values;
   private final Function<Object, String> nameFunction;
   private final Function<Object, Object> jsonFunction;

   public <T extends Comparable<T>> BlockProperty(String name, Class<T> type, T defaultValue, Collection<T> values, Function<T, String> nameFunction) {
      this.name = var1;
      this.type = var2;
      this.defaultValue = var3;
      this.values = Collections.unmodifiableSet(new TreeSet(var4));
      this.nameFunction = (Function)var5;
      Function var10001;
      if (NATIVES.contains(var2)) {
         var10001 = Function.identity();
      } else {
         var10001 = this.nameFunction;
         Objects.requireNonNull(var10001);
         var10001 = var10001::apply;
      }

      this.jsonFunction = var10001;
   }

   public static <T extends Enum<T>> BlockProperty ofEnum(Class<T> type, String name, T defaultValue) {
      return new BlockProperty(var1, var0, var2, Arrays.asList((Enum[])var0.getEnumConstants()), (var0x) -> {
         return var0x == null ? "null" : var0x.name();
      });
   }

   public static BlockProperty ofFloat(String name, float defaultValue, float min, float max, boolean exclusiveMin, boolean exclusiveMax) {
      return new BlockProperty.BoundedDouble(var0, (double)var1, (double)var2, (double)var3, var4, var5, (var0x) -> {
         return String.format("%.2f", var0x);
      });
   }

   public static BlockProperty ofBoolean(String name, boolean defaultValue) {
      return new BlockProperty(var0, Boolean.class, var1, List.of(true, false), (var0x) -> {
         return var0x ? "true" : "false";
      });
   }

   @NotNull
   public String toString() {
      String var10000 = this.name;
      return var10000 + "=" + (String)this.nameFunction.apply(this.defaultValue) + " [" + String.join(",", this.names()) + "]";
   }

   public String name() {
      return this.name;
   }

   public String defaultValue() {
      return (String)this.nameFunction.apply(this.defaultValue);
   }

   public List<String> names() {
      return this.values.stream().map(this.nameFunction).toList();
   }

   public Object defaultValueAsJson() {
      return this.jsonFunction.apply(this.defaultValue);
   }

   public JSONArray valuesAsJson() {
      return new JSONArray(this.values.stream().map(this.jsonFunction).toList());
   }

   public JSONObject buildJson() {
      JSONObject var1 = new JSONObject();
      var1.put("type", (Object)this.jsonType());
      var1.put("default", this.defaultValueAsJson());
      if (!this.values.isEmpty()) {
         var1.put("enum", (Object)this.valuesAsJson());
      }

      return var1;
   }

   public String jsonType() {
      if (this.type == Boolean.class) {
         return "boolean";
      } else if (this.type != Byte.class && this.type != Short.class && this.type != Integer.class && this.type != Long.class) {
         return this.type != Float.class && this.type != Double.class ? "string" : "number";
      } else {
         return "integer";
      }
   }

   public boolean equals(Object obj) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1.getClass() == this.getClass()) {
         BlockProperty var2 = (BlockProperty)var1;
         return Objects.equals(this.name, var2.name) && Objects.equals(this.values, var2.values) && Objects.equals(this.type, var2.type);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.values, this.type});
   }

   private static class BoundedDouble extends BlockProperty {
      private final double min;
      private final double max;
      private final boolean exclusiveMin;
      private final boolean exclusiveMax;

      public BoundedDouble(String name, double defaultValue, double min, double max, boolean exclusiveMin, boolean exclusiveMax, Function<Double, String> nameFunction) {
         super(var1, Double.class, var2, List.of(), var10);
         this.min = var4;
         this.max = var6;
         this.exclusiveMin = var8;
         this.exclusiveMax = var9;
      }

      public JSONObject buildJson() {
         return super.buildJson().put("minimum", this.min).put("maximum", this.max).put("exclusiveMinimum", this.exclusiveMin).put("exclusiveMaximum", this.exclusiveMax);
      }
   }
}
