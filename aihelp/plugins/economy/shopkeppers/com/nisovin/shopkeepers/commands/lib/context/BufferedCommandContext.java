package com.nisovin.shopkeepers.commands.lib.context;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BufferedCommandContext extends SimpleCommandContext {
   private final CommandContext context;

   public BufferedCommandContext(CommandContext context) {
      Validate.notNull(context, (String)"context is null");
      this.context = context;
   }

   public CommandContext getParentContext() {
      return this.context;
   }

   public void clearBuffer() {
      this.values.clear();
   }

   public void applyBuffer() {
      this.applyBuffer(this.context);
      this.clearBuffer();
   }

   public void applyBuffer(CommandContext context) {
      this.values.forEach((key, value) -> {
         context.put(key, value);
      });
   }

   public <T> T getOrNull(String key) {
      T value = super.getOrNull(key);
      return value != null ? value : this.context.getOrNull(key);
   }

   public boolean has(String key) {
      return super.has(key) || this.context.has(key);
   }

   public Map<? extends String, ?> getMapView() {
      if (this.values.isEmpty()) {
         return this.context.getMapView();
      } else {
         Map<String, Object> combined = new LinkedHashMap(this.context.getMapView());
         combined.putAll(super.values);
         return Collections.unmodifiableMap(combined);
      }
   }

   public CommandContextView getView() {
      return super.getView();
   }

   public CommandContext copy() {
      return super.copy();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("BufferedCommandContext [context=");
      builder.append(this.context);
      builder.append(", buffer=");
      builder.append(this.values);
      builder.append("]");
      return builder.toString();
   }
}
