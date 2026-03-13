package com.nisovin.shopkeepers.commands.lib.context;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CommandContextView implements CommandContext {
   private final CommandContext context;

   public CommandContextView(CommandContext context) {
      Validate.notNull(context, (String)"context is null");
      this.context = context;
   }

   public void put(String key, Object value) {
      throw new UnsupportedOperationException("This CommandContext does not allow modifications!");
   }

   @NonNull
   public <T> T get(String key) {
      return this.context.get(key);
   }

   @Nullable
   public <T> T getOrNull(String key) {
      return this.context.getOrNull(key);
   }

   @Nullable
   public <T> T getOrDefault(String key, @Nullable T defaultValue) {
      return this.context.getOrDefault(key, defaultValue);
   }

   @Nullable
   public <T> T getOrDefault(String key, Supplier<T> defaultValueSupplier) {
      return this.context.getOrDefault(key, defaultValueSupplier);
   }

   public boolean has(String key) {
      return this.context.has(key);
   }

   public Map<? extends String, ?> getMapView() {
      return this.context.getMapView();
   }

   public CommandContextView getView() {
      return this;
   }

   public CommandContext copy() {
      return this.context.copy();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CommandContextView [context=");
      builder.append(this.context);
      builder.append("]");
      return builder.toString();
   }
}
