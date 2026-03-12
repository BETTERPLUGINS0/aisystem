package ac.grim.grimac.shaded.kyori.adventure.nbt;

import java.util.Collections;

final class ListBinaryTag0 {
   private static final String WRAPPER_KEY = "";

   private ListBinaryTag0() {
   }

   static BinaryTag unbox(final CompoundBinaryTag compound) {
      if (compound.size() == 1) {
         BinaryTag potentialValue = compound.get("");
         if (potentialValue != null) {
            return potentialValue;
         }
      }

      return compound;
   }

   static CompoundBinaryTag box(final BinaryTag tag) {
      return (CompoundBinaryTag)(needsBox(tag) ? new CompoundBinaryTagImpl(Collections.singletonMap("", tag)) : (CompoundBinaryTag)tag);
   }

   private static boolean needsBox(final BinaryTag tag) {
      if (!(tag instanceof CompoundBinaryTag)) {
         return true;
      } else {
         CompoundBinaryTag compound = (CompoundBinaryTag)tag;
         return compound.size() == 1 && compound.get("") != null;
      }
   }
}
