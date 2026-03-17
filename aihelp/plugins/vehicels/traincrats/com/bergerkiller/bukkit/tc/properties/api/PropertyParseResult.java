package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyInputContext;
import org.bukkit.command.CommandSender;

public class PropertyParseResult<T> {
   private final PropertyInputContext inputContext;
   private final IProperty<T> property;
   private final String name;
   private final T value;
   private final PropertyParseResult.Reason reason;
   private final String message;

   private PropertyParseResult(PropertyInputContext inputContext, IProperty<T> property, String name, T value, PropertyParseResult.Reason reason, String message) {
      this.inputContext = inputContext;
      this.property = property;
      this.name = name;
      this.value = value;
      this.reason = reason;
      this.message = message;
   }

   public boolean isSuccessful() {
      return this.reason == PropertyParseResult.Reason.NONE;
   }

   public PropertyInputContext getInputContext() {
      return this.inputContext;
   }

   public PropertyParseResult.Reason getReason() {
      return this.reason;
   }

   public String getMessage() {
      return this.message;
   }

   public IProperty<T> getProperty() {
      return this.property;
   }

   public String getName() {
      return this.name;
   }

   public T getValue() {
      return this.value;
   }

   public boolean hasPermission(CommandSender sender) {
      return this.reason == PropertyParseResult.Reason.PROPERTY_NOT_FOUND || this.property.hasPermission(sender, this.name);
   }

   public static <T> PropertyParseResult<T> failPropertyNotFound(PropertyInputContext inputContext, String name) {
      return new PropertyParseResult(inputContext, (IProperty)null, name, (Object)null, PropertyParseResult.Reason.PROPERTY_NOT_FOUND, Localization.PROPERTY_NOTFOUND.get(name));
   }

   public static <T> PropertyParseResult<T> failInvalidInput(PropertyInputContext inputContext, IProperty<T> property, String name, String message) {
      return new PropertyParseResult(inputContext, property, name, (Object)null, PropertyParseResult.Reason.INVALID_INPUT, message);
   }

   public static <T> PropertyParseResult<T> failError(PropertyInputContext inputContext, IProperty<T> property, String name) {
      return new PropertyParseResult(inputContext, property, name, (Object)null, PropertyParseResult.Reason.ERROR, Localization.PROPERTY_ERROR.get(name, inputContext.input()));
   }

   public static <T> PropertyParseResult<T> failSuppressed(PropertyInputContext inputContext, IProperty<T> property, String name) {
      return new PropertyParseResult(inputContext, property, name, (Object)null, PropertyParseResult.Reason.SUPPRESSED, "Suppressed");
   }

   public static <T> PropertyParseResult<T> success(PropertyInputContext inputContext, IProperty<T> property, String name, T value) {
      return new PropertyParseResult(inputContext, property, name, value, PropertyParseResult.Reason.NONE, "");
   }

   public static enum Reason {
      NONE,
      PROPERTY_NOT_FOUND,
      INVALID_INPUT,
      SUPPRESSED,
      ERROR;

      // $FF: synthetic method
      private static PropertyParseResult.Reason[] $values() {
         return new PropertyParseResult.Reason[]{NONE, PROPERTY_NOT_FOUND, INVALID_INPUT, SUPPRESSED, ERROR};
      }
   }
}
