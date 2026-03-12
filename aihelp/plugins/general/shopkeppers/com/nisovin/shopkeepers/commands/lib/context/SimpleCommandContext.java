package com.nisovin.shopkeepers.commands.lib.context;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SimpleCommandContext implements CommandContext {
   protected final Map<String, Object> values = new LinkedHashMap();
   private final CommandContextView view = new CommandContextView((CommandContext)Unsafe.initialized(this));

   public void put(String key, Object value) {
      Validate.notEmpty(key, "key is null or empty");
      Validate.notNull(value, "value is null");
      this.values.put(key, value);
   }

   public <T> T get(String key) {
      T value = this.getOrNull(key);
      Validate.State.notNull(value, () -> {
         return "Missing value for key '" + key + "'.";
      });
      return Unsafe.assertNonNull(value);
   }

   @Nullable
   public <T> T getOrNull(String key) {
      return this.values.get(key);
   }

   public <T> T getOrDefault(String key, T defaultValue) {
      T value = this.getOrNull(key);
      return value != null ? value : defaultValue;
   }

   public <T> T getOrDefault(String key, Supplier<T> defaultValueSupplier) {
      T value = this.getOrNull(key);
      if (value != null) {
         return value;
      } else {
         Validate.notNull(defaultValueSupplier, (String)"defaultValueSupplier is null");

         assert defaultValueSupplier != null;

         return defaultValueSupplier.get();
      }
   }

   public boolean has(String key) {
      return this.values.containsKey(key);
   }

   public Map<? extends String, ?> getMapView() {
      return Collections.unmodifiableMap(this.values);
   }

   public CommandContextView getView() {
      return this.view;
   }

   public CommandContext copy() {
      SimpleCommandContext copy = new SimpleCommandContext();
      copy.values.putAll(this.getMapView());
      return copy;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SimpleCommandContext [values=");
      builder.append(this.values);
      builder.append("]");
      return builder.toString();
   }
}
