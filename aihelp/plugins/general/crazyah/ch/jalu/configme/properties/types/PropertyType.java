package ch.jalu.configme.properties.types;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PropertyType<T> {
   @Nullable
   T convert(@Nullable Object var1, @NotNull ConvertErrorRecorder var2);

   @Nullable
   Object toExportValue(@Nullable T var1);
}
