package fr.xephi.authme.libs.ch.jalu.configme.properties.types;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import javax.annotation.Nullable;

public interface PropertyType<T> {
   @Nullable
   T convert(@Nullable Object var1, ConvertErrorRecorder var2);

   Object toExportValue(T var1);
}
