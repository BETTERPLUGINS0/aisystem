package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

public class EnumLeafValueHandler extends AbstractLeafValueHandler {
   public Object convert(Class<?> clazz, Object value) {
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

   public Object toExportValue(Object value) {
      return value instanceof Enum ? ((Enum)value).name() : null;
   }
}
