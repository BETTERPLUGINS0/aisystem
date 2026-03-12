package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class VillagerVariantComponent {
   private VillagerType villagerType;

   public VillagerVariantComponent(VillagerType villagerType) {
      this.villagerType = villagerType;
   }

   public static VillagerVariantComponent read(PacketWrapper<?> wrapper) {
      VillagerType type = (VillagerType)wrapper.readMappedEntity((IRegistry)VillagerTypes.getRegistry());
      return new VillagerVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, VillagerVariantComponent component) {
      wrapper.writeMappedEntity(component.villagerType);
   }

   public VillagerType getVillagerType() {
      return this.villagerType;
   }

   public void setVillagerType(VillagerType villagerType) {
      this.villagerType = villagerType;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof VillagerVariantComponent)) {
         return false;
      } else {
         VillagerVariantComponent that = (VillagerVariantComponent)obj;
         return this.villagerType.equals(that.villagerType);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.villagerType);
   }
}
