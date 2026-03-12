package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.examination.Examinable;

public interface DataComponentValue extends Examinable {
   @NotNull
   static DataComponentValue.Removed removed() {
      return RemovedDataComponentValueImpl.REMOVED;
   }

   public interface Removed extends DataComponentValue {
   }

   public interface TagSerializable extends DataComponentValue {
      @NotNull
      BinaryTagHolder asBinaryTag();
   }
}
