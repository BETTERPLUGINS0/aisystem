package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Objects;
import javax.annotation.Nullable;

public abstract class BaseProperty<T> implements Property<T> {
   private final String path;
   private final T defaultValue;

   public BaseProperty(String path, T defaultValue) {
      Objects.requireNonNull(path, "path");
      Objects.requireNonNull(defaultValue, "defaultValue");
      this.path = path;
      this.defaultValue = defaultValue;
   }

   public String getPath() {
      return this.path;
   }

   public T getDefaultValue() {
      return this.defaultValue;
   }

   public PropertyValue<T> determineValue(PropertyReader reader) {
      ConvertErrorRecorder errorRecorder = new ConvertErrorRecorder();
      T value = this.getFromReader(reader, errorRecorder);
      return this.isValidValue(value) ? new PropertyValue(value, errorRecorder.isFullyValid()) : PropertyValue.withValueRequiringRewrite(this.getDefaultValue());
   }

   public boolean isValidValue(T value) {
      return value != null;
   }

   @Nullable
   protected abstract T getFromReader(PropertyReader var1, ConvertErrorRecorder var2);

   public String toString() {
      return "Property '" + this.path + "'";
   }
}
