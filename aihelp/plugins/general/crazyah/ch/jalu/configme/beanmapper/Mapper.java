package ch.jalu.configme.beanmapper;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.utils.TypeInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Mapper {
   @Nullable
   Object convertToBean(@Nullable Object var1, @NotNull TypeInformation var2, @NotNull ConvertErrorRecorder var3);

   @Nullable
   default <T> T convertToBean(@Nullable Object value, @NotNull Class<T> clazz, @NotNull ConvertErrorRecorder errorRecorder) {
      return this.convertToBean(value, new TypeInformation(clazz), errorRecorder);
   }

   @Nullable
   Object toExportValue(@Nullable Object var1);
}
