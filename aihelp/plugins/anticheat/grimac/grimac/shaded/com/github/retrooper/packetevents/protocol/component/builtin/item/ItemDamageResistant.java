package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemDamageResistant {
   private ResourceLocation typesTagKey;

   public ItemDamageResistant(ResourceLocation typesTagKey) {
      this.typesTagKey = typesTagKey;
   }

   public static ItemDamageResistant read(PacketWrapper<?> wrapper) {
      return new ItemDamageResistant(wrapper.readIdentifier());
   }

   public static void write(PacketWrapper<?> wrapper, ItemDamageResistant resistant) {
      wrapper.writeIdentifier(resistant.typesTagKey);
   }

   public ResourceLocation getTypesTagKey() {
      return this.typesTagKey;
   }

   public void setTypesTagKey(ResourceLocation typesTagKey) {
      this.typesTagKey = typesTagKey;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemDamageResistant)) {
         return false;
      } else {
         ItemDamageResistant that = (ItemDamageResistant)obj;
         return this.typesTagKey.equals(that.typesTagKey);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.typesTagKey);
   }

   public String toString() {
      return "ItemDamageResistant{typesTagKey=" + this.typesTagKey + '}';
   }
}
