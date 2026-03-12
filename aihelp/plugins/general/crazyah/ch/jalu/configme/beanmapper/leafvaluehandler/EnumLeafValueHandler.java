package ch.jalu.configme.beanmapper.leafvaluehandler;

import org.jetbrains.annotations.Nullable;

public class EnumLeafValueHandler extends AbstractLeafValueHandler {
   @Nullable
   public Object convert(@Nullable Class<?> clazz, @Nullable Object value) {
      if (value instanceof String && Enum.class.isAssignableFrom(clazz)) {
         String givenText = (String)value;
         Enum[] var4 = (Enum[])((Enum[])clazz.getEnumConstants());
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Enum e = var4[var6];
            if (e.name().equalsIgnoreCase(givenText)) {
               return e;
            }
         }
      }

      return null;
   }

   @Nullable
   public Object toExportValue(@Nullable Object value) {
      return value instanceof Enum ? ((Enum)value).name() : null;
   }
}
