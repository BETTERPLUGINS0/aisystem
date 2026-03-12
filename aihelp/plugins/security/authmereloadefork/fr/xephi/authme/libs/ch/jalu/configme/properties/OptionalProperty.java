package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Optional;

public class OptionalProperty<T> implements Property<Optional<T>> {
   private final Property<T> baseProperty;
   private final Optional<T> defaultValue;

   public OptionalProperty(Property<T> baseProperty) {
      this.baseProperty = baseProperty;
      this.defaultValue = Optional.empty();
   }

   public OptionalProperty(Property<T> baseProperty, T defaultValue) {
      this.baseProperty = baseProperty;
      this.defaultValue = Optional.of(defaultValue);
   }

   public String getPath() {
      return this.baseProperty.getPath();
   }

   public PropertyValue<Optional<T>> determineValue(PropertyReader reader) {
      PropertyValue<T> basePropertyValue = this.baseProperty.determineValue(reader);
      Optional<T> value = basePropertyValue.isValidInResource() ? Optional.ofNullable(basePropertyValue.getValue()) : Optional.empty();
      boolean isWrongInResource = !basePropertyValue.isValidInResource() && reader.contains(this.baseProperty.getPath());
      return isWrongInResource ? PropertyValue.withValueRequiringRewrite(value) : PropertyValue.withValidValue(value);
   }

   public Optional<T> getDefaultValue() {
      return this.defaultValue;
   }

   public boolean isValidValue(Optional<T> value) {
      if (value == null) {
         return false;
      } else {
         Property var10001 = this.baseProperty;
         var10001.getClass();
         return (Boolean)value.map(var10001::isValidValue).orElse(true);
      }
   }

   public Object toExportValue(Optional<T> value) {
      Property var10001 = this.baseProperty;
      var10001.getClass();
      return value.map(var10001::toExportValue).orElse((Object)null);
   }
}
