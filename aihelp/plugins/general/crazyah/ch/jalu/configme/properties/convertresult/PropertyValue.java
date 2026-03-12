package ch.jalu.configme.properties.convertresult;

import org.jetbrains.annotations.NotNull;

public class PropertyValue<T> {
   private final T value;
   private final boolean isValidInResource;

   public PropertyValue(@NotNull T value, boolean isValidInResource) {
      this.value = value;
      this.isValidInResource = isValidInResource;
   }

   @NotNull
   public static <T> PropertyValue<T> withValidValue(@NotNull T value) {
      return new PropertyValue(value, true);
   }

   @NotNull
   public static <T> PropertyValue<T> withValueRequiringRewrite(@NotNull T value) {
      return new PropertyValue(value, false);
   }

   @NotNull
   public T getValue() {
      return this.value;
   }

   public boolean isValidInResource() {
      return this.isValidInResource;
   }

   @NotNull
   public String toString() {
      return "PropertyValue[valid=" + this.isValidInResource + ", value='" + this.value + "']";
   }
}
