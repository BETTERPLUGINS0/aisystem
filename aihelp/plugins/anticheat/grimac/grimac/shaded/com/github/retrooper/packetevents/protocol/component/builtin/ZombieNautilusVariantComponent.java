package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ZombieNautilusVariantComponent {
   private MaybeMappedEntity<ZombieNautilusVariant> variant;

   public ZombieNautilusVariantComponent(MaybeMappedEntity<ZombieNautilusVariant> variant) {
      this.variant = variant;
   }

   public static ZombieNautilusVariantComponent read(PacketWrapper<?> wrapper) {
      MaybeMappedEntity<ZombieNautilusVariant> variant = MaybeMappedEntity.read(wrapper, ZombieNautilusVariants.getRegistry(), ZombieNautilusVariant::read);
      return new ZombieNautilusVariantComponent(variant);
   }

   public static void write(PacketWrapper<?> wrapper, ZombieNautilusVariantComponent component) {
      MaybeMappedEntity.write(wrapper, component.variant, ZombieNautilusVariant::write);
   }

   public MaybeMappedEntity<ZombieNautilusVariant> getVariant() {
      return this.variant;
   }

   public void setVariant(MaybeMappedEntity<ZombieNautilusVariant> variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ZombieNautilusVariantComponent)) {
         return false;
      } else {
         ZombieNautilusVariantComponent that = (ZombieNautilusVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
