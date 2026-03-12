package fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult;

public class PropertyValue<T> {
   private final T value;
   private final boolean isValidInResource;

   public PropertyValue(T value, boolean isValidInResource) {
      this.value = value;
      this.isValidInResource = isValidInResource;
   }

   public static <T> PropertyValue<T> withValidValue(T value) {
      return new PropertyValue(value, true);
   }

   public static <T> PropertyValue<T> withValueRequiringRewrite(T value) {
      return new PropertyValue(value, false);
   }

   public T getValue() {
      return this.value;
   }

   public boolean isValidInResource() {
      return this.isValidInResource;
   }

   public String toString() {
      return "PropertyValue[valid=" + this.isValidInResource + ", value='" + this.value + "']";
   }
}
