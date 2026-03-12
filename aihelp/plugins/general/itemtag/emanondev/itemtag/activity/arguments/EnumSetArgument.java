package emanondev.itemtag.activity.arguments;

import java.util.EnumSet;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public class EnumSetArgument<E extends Enum<E>> extends Argument {
   private final EnumSet<E> values;
   private final String separator;
   private final Class<E> clazz;

   public EnumSetArgument(String info, @NotNull Class<E> clazz) {
      this(info, ";", clazz);
   }

   public EnumSetArgument(String info, String separator, @NotNull Class<E> clazz) {
      boolean reversed = info.startsWith("!");
      this.separator = separator;
      this.clazz = clazz;
      if (reversed) {
         info = info.substring(1);
      }

      if (reversed) {
         this.values = EnumSet.allOf(clazz);
      } else {
         this.values = EnumSet.noneOf(clazz);
      }

      String[] var5 = info.split(separator);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String arg = var5[var7];

         try {
            this.values.add(Enum.valueOf(clazz, arg));
         } catch (Exception var12) {
            try {
               this.values.add(Enum.valueOf(clazz, arg.toUpperCase(Locale.ENGLISH)));
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         }
      }

   }

   public boolean contains(E value) {
      return this.values.contains(value);
   }

   public String toString() {
      StringBuilder standard = new StringBuilder();
      StringBuilder reversed = new StringBuilder("!");
      Enum[] var3 = (Enum[])this.clazz.getEnumConstants();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         E e = var3[var5];
         if (this.values.contains(e)) {
            if (standard.length() != 0) {
               standard.append(this.separator);
            }

            standard.append(e.name());
         } else {
            if (reversed.length() != 1) {
               reversed.append(this.separator);
            }

            reversed.append(e.name());
         }
      }

      return (standard.length() <= reversed.length() ? standard : reversed).toString();
   }
}
