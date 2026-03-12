package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.axolotl.AxolotlVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.axolotl.AxolotlVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class AxolotlVariantComponent {
   private AxolotlVariant variant;

   public AxolotlVariantComponent(AxolotlVariant variant) {
      this.variant = variant;
   }

   public static AxolotlVariantComponent read(PacketWrapper<?> wrapper) {
      AxolotlVariant type = (AxolotlVariant)wrapper.readMappedEntity((IRegistry)AxolotlVariants.getRegistry());
      return new AxolotlVariantComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, AxolotlVariantComponent component) {
      wrapper.writeMappedEntity(component.variant);
   }

   public AxolotlVariant getVariant() {
      return this.variant;
   }

   public void setVariant(AxolotlVariant variant) {
      this.variant = variant;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof AxolotlVariantComponent)) {
         return false;
      } else {
         AxolotlVariantComponent that = (AxolotlVariantComponent)obj;
         return this.variant.equals(that.variant);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.variant);
   }
}
