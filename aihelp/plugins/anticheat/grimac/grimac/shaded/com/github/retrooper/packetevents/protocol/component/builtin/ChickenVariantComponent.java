package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ChickenVariantComponent {
   private MaybeMappedEntity<ChickenVariant> variant;

   public ChickenVariantComponent(MaybeMappedEntity<ChickenVariant> variant) {
      this.variant = variant;
   }

   public static ChickenVariantComponent read(PacketWrapper<?> wrapper) {
      MaybeMappedEntity<ChickenVariant> variant = MaybeMappedEntity.read(wrapper, ChickenVariants.getRegistry(), ChickenVariant::read);
      return new ChickenVariantComponent(variant);
   }

   public static void write(PacketWrapper<?> wrapper, ChickenVariantComponent component) {
      MaybeMappedEntity.write(wrapper, component.variant, ChickenVariant::write);
   }

   public MaybeMappedEntity<ChickenVariant> getVariant() {
      return this.variant;
   }

   public void setVariant(MaybeMappedEntity<ChickenVariant> variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ChickenVariantComponent)) {
         return false;
      } else {
         ChickenVariantComponent that = (ChickenVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
