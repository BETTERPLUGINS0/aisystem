package ch.jalu.configme.properties;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.convertresult.PropertyValue;
import ch.jalu.configme.resource.PropertyReader;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseProperty<T> implements Property<T> {
   private final String path;
   private final T defaultValue;

   public BaseProperty(@NotNull String path, @NotNull T defaultValue) {
      Objects.requireNonNull(path, "path");
      Objects.requireNonNull(defaultValue, "defaultValue");
      this.path = path;
      this.defaultValue = defaultValue;
   }

   @NotNull
   public String getPath() {
      return this.path;
   }

   @NotNull
   public T getDefaultValue() {
      return this.defaultValue;
   }

   @NotNull
   public PropertyValue<T> determineValue(@NotNull PropertyReader reader) {
      ConvertErrorRecorder errorRecorder = new ConvertErrorRecorder();
      T value = this.getFromReader(reader, errorRecorder);
      return this.isValidValue(value) ? new PropertyValue(value, errorRecorder.isFullyValid()) : PropertyValue.withValueRequiringRewrite(this.getDefaultValue());
   }

   public boolean isValidValue(@Nullable T value) {
      return value != null;
   }

   @Nullable
   protected abstract T getFromReader(@NotNull PropertyReader var1, @NotNull ConvertErrorRecorder var2);

   @NotNull
   public String toString() {
      return "Property '" + this.path + "'";
   }
}
