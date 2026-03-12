package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemSwingAnimation {
   private static final ItemSwingAnimation.Type[] TYPES = ItemSwingAnimation.Type.values();
   private ItemSwingAnimation.Type type;
   private int duration;

   public ItemSwingAnimation(ItemSwingAnimation.Type type, int duration) {
      this.type = type;
      this.duration = duration;
   }

   public static ItemSwingAnimation read(PacketWrapper<?> wrapper) {
      ItemSwingAnimation.Type type = (ItemSwingAnimation.Type)wrapper.readEnum((Enum[])TYPES, ItemSwingAnimation.Type.NONE);
      int duration = wrapper.readVarInt();
      return new ItemSwingAnimation(type, duration);
   }

   public static void write(PacketWrapper<?> wrapper, ItemSwingAnimation component) {
      wrapper.writeEnum(component.type);
      wrapper.writeVarInt(component.duration);
   }

   public ItemSwingAnimation.Type getType() {
      return this.type;
   }

   public void setType(ItemSwingAnimation.Type type) {
      this.type = type;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ItemSwingAnimation that = (ItemSwingAnimation)obj;
         if (this.duration != that.duration) {
            return false;
         } else {
            return this.type == that.type;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.duration});
   }

   public static enum Type {
      NONE,
      WHACK,
      STAB;

      // $FF: synthetic method
      private static ItemSwingAnimation.Type[] $values() {
         return new ItemSwingAnimation.Type[]{NONE, WHACK, STAB};
      }
   }
}
