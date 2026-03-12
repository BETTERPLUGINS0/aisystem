package ch.jalu.configme.properties;

import ch.jalu.configme.properties.convertresult.PropertyValue;
import ch.jalu.configme.resource.PropertyReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Property<T> {
   @NotNull
   String getPath();

   @NotNull
   PropertyValue<T> determineValue(@NotNull PropertyReader var1);

   default boolean isValidInResource(@NotNull PropertyReader propertyReader) {
      return this.determineValue(propertyReader).isValidInResource();
   }

   @NotNull
   T getDefaultValue();

   boolean isValidValue(@Nullable T var1);

   @Nullable
   Object toExportValue(@NotNull T var1);
}
