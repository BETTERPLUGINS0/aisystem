package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class WolfVariantComponent {
   private WolfVariant variant;

   public WolfVariantComponent(WolfVariant variant) {
      this.variant = variant;
   }

   public static WolfVariantComponent read(PacketWrapper<?> wrapper) {
      WolfVariant type = (WolfVariant)wrapper.readMappedEntity((IRegistry)WolfVariants.getRegistry());
      return new WolfVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, WolfVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public WolfVariant getVariant() {
      return this.variant;
   }

   public void setVariant(WolfVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof WolfVariantComponent)) {
         return false;
      } else {
         WolfVariantComponent that = (WolfVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
