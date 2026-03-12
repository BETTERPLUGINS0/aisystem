package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface NumberBinaryTag extends BinaryTag {
   @NotNull
   BinaryTagType<? extends NumberBinaryTag> type();

   byte byteValue();

   double doubleValue();

   float floatValue();

   int intValue();

   long longValue();

   short shortValue();

   @NotNull
   Number numberValue();
}
