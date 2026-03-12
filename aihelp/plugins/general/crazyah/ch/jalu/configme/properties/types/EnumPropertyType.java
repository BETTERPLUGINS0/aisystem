package ch.jalu.configme.properties.types;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnumPropertyType<E extends Enum<E>> implements PropertyType<E> {
   private Class<E> enumType;

   public EnumPropertyType(@NotNull Class<E> enumType) {
      this.enumType = enumType;
   }

   @NotNull
   public static <E extends Enum<E>> EnumPropertyType<E> of(@NotNull Class<E> type) {
      return new EnumPropertyType(type);
   }

   @Nullable
   public E convert(@Nullable Object object, @NotNull ConvertErrorRecorder errorRecorder) {
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

   @NotNull
   public Object toExportValue(@NotNull E value) {
      return value.name();
   }
}
