package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ItemDeathProtection {
   private List<ConsumeEffect<?>> deathEffects;

   public ItemDeathProtection(List<ConsumeEffect<?>> deathEffects) {
      this.deathEffects = deathEffects;
   }

   public static ItemDeathProtection read(PacketWrapper<?> wrapper) {
      List<ConsumeEffect<?>> deathEffects = wrapper.readList(ConsumeEffect::readFull);
      return new ItemDeathProtection(deathEffects);
   }

   public static void write(PacketWrapper<?> wrapper, ItemDeathProtection deathProtection) {
      wrapper.writeList(deathProtection.deathEffects, ConsumeEffect::writeFull);
   }

   public List<ConsumeEffect<?>> getDeathEffects() {
      return this.deathEffects;
   }

   public void setDeathEffects(List<ConsumeEffect<?>> deathEffects) {
      this.deathEffects = deathEffects;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemDeathProtection)) {
         return false;
      } else {
         ItemDeathProtection that = (ItemDeathProtection)obj;
         return this.deathEffects.equals(that.deathEffects);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.deathEffects);
   }

   public String toString() {
      return "ItemDeathProtection{deathEffects=" + this.deathEffects + '}';
   }
}
