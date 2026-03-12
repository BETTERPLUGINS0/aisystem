package fr.xephi.authme.libs.ch.jalu.configme.properties.types;

import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;

public class EnumPropertyType<E extends Enum<E>> implements PropertyType<E> {
   private Class<E> enumType;

   public EnumPropertyType(Class<E> enumType) {
      this.enumType = enumType;
   }

   public static <E extends Enum<E>> EnumPropertyType<E> of(Class<E> type) {
      return new EnumPropertyType(type);
   }

   public E convert(Object object, ConvertErrorRecorder errorRecorder) {
      if (this.enumType.isInstance(object)) {
         return (Enum)object;
      } else if (!(object instanceof String)) {
         return null;
      } else {
         String name = (String)object;
         Enum[] var4 = (Enum[])this.enumType.getEnumConstants();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            E entry = var4[var6];
            if (entry.name().equalsIgnoreCase(name)) {
               return entry;
            }
         }

         return null;
      }
   }

   public Object toExportValue(E value) {
      return value.name();
   }
}
