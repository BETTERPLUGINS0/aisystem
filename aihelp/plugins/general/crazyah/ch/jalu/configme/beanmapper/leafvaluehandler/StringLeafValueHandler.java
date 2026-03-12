package ch.jalu.configme.beanmapper.leafvaluehandler;

import org.jetbrains.annotations.Nullable;

public class StringLeafValueHandler extends AbstractLeafValueHandler {
   @Nullable
   public Object convert(@Nullable Class<?> clazz, @Nullable Object value) {
      return clazz != String.class || !(value instanceof String) && !(value instanceof Number) && !(value instanceof Boolean) ? null : value.toString();
   }

   @Nullable
   public Object toExportValue(@Nullable Object value) {
      return value instanceof String ? value : null;
   }
}
