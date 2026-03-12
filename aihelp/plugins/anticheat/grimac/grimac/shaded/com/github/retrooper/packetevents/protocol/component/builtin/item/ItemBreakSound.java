package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemBreakSound {
   private Sound sound;

   public ItemBreakSound(Sound sound) {
      this.sound = sound;
   }

   public static ItemBreakSound read(PacketWrapper<?> wrapper) {
      Sound sound = Sound.read(wrapper);
      return new ItemBreakSound(sound);
   }

   public static void write(PacketWrapper<?> wrapper, ItemBreakSound sound) {
      Sound.write(wrapper, sound.sound);
   }

   public Sound getSound() {
      return this.sound;
   }

   public void setSound(Sound sound) {
      this.sound = sound;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemBreakSound)) {
         return false;
      } else {
         ItemBreakSound that = (ItemBreakSound)obj;
         return this.sound.equals(that.sound);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.sound);
   }
}
