package advancedplugins.pm2.cv.api.vehicle.controller;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleControllerProperties implements ConfigurationSectionWritable {
   @NotNull
   protected final Map<String, Object> values;

   public static VehicleControllerProperties load(@NotNull ConfigurationSection var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = var0.getValues(false).entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         Object var4 = var3.getValue();
         if (var4 != null) {
            var1.put(IDeyed.idCheck((String)var3.getKey()), var4);
         }
      }

      return new VehicleControllerProperties(var1);
   }

   public VehicleControllerProperties(@Nullable Map<String, Object> var1) {
      this.values = new HashMap();
      if (var1 != null) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.values.put(IDeyed.idCheck((String)var3.getKey()), var3.getValue());
         }
      }

   }

   public VehicleControllerProperties() {
      this((Map)null);
   }

   public Map<String, Object> getValues() {
      return Collections.unmodifiableMap(this.values);
   }

   public String getStringProperty(@NotNull String var1, @Nullable String var2) {
      return (String)this.getProperty(String.class, var1, var2);
   }

   public boolean getBooleanProperty(@NotNull String var1, boolean var2) {
      return (Boolean)this.getProperty(Boolean.class, var1, var2);
   }

   public int getIntegerProperty(@NotNull String var1, int var2) {
      return this.getNumericProperty(var1, var2).intValue();
   }

   public float getFloatProperty(@NotNull String var1, float var2) {
      return this.getNumericProperty(var1, var2).floatValue();
   }

   public double getDoubleProperty(@NotNull String var1, double var2) {
      return this.getNumericProperty(var1, var2).doubleValue();
   }

   public double getDoubleProperty(@NotNull String var1, double var2, double var4) {
      double var6 = this.getNumericProperty(var1, var2).doubleValue();
      return var6 * (var6 != var2 ? var4 : 1.0D);
   }

   public Number getNumericProperty(@NotNull String var1, @Nullable Number var2) {
      return (Number)this.getProperty(Number.class, var1, var2);
   }

   public <T extends Enum<T>> T getEnumProperty(@NotNull String var1, @NotNull Class<T> var2, @Nullable T var3) {
      Object var4 = this.values.get(var1);
      if (var4 instanceof String) {
         Enum var5 = EnumReflection.getEnumConstant(var2, (String)var4);
         if (var5 != null) {
            return var5;
         }
      }

      return var3;
   }

   public <T> T getProperty(@NotNull Class<T> var1, @NotNull String var2, @Nullable T var3) {
      Object var4 = this.values.get(var2);
      return var4 != null && var1.isAssignableFrom(var4.getClass()) ? var1.cast(var4) : var3;
   }

   public boolean containsProperty(@NotNull String var1) {
      return this.values.containsKey(var1);
   }

   public float getMinFuelConsumptionOverride(float var1) {
      return this.getFloatProperty("min-fuel-consumption", var1);
   }

   public float getMaxFuelConsumptionOverride(float var1) {
      return this.getFloatProperty("max-fuel-consumption", var1);
   }

   public void write(@NotNull ConfigurationSection var1) {
      Iterator var2 = this.values.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.set((String)var3.getKey(), var3.getValue());
      }

   }

   public void merge(@NotNull VehicleControllerProperties var1) {
      var1.values.forEach((var1x, var2) -> {
         this.values.put(var1x, this.values.containsKey(var1x) ? this.mergeValues(this.values.get(var1x), var2) : var2);
      });
   }

   private Object mergeValues(Object var1, Object var2) {
      return var1 instanceof Number && var2 instanceof Number ? ((Number)var1).doubleValue() + ((Number)var2).doubleValue() : var2;
   }
}
