package fr.xephi.authme.libs.ch.jalu.configme.beanmapper;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;
import javax.annotation.Nullable;

public interface Mapper {
   @Nullable
   Object convertToBean(@Nullable Object var1, TypeInformation var2, ConvertErrorRecorder var3);

   @Nullable
   default <T> T convertToBean(@Nullable Object value, Class<T> clazz, ConvertErrorRecorder errorRecorder) {
      return this.convertToBean(value, new TypeInformation(clazz), errorRecorder);
   }

   @Nullable
   Object toExportValue(@Nullable Object var1);
}
