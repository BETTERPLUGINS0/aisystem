package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemProvidesTrimMaterial {
   private MaybeMappedEntity<TrimMaterial> material;

   public ItemProvidesTrimMaterial(MaybeMappedEntity<TrimMaterial> material) {
      this.material = material;
   }

   public static ItemProvidesTrimMaterial read(PacketWrapper<?> wrapper) {
      MaybeMappedEntity<TrimMaterial> material = MaybeMappedEntity.read(wrapper, TrimMaterials.getRegistry(), TrimMaterial::read);
      return new ItemProvidesTrimMaterial(material);
   }

   public static void write(PacketWrapper<?> wrapper, ItemProvidesTrimMaterial material) {
      MaybeMappedEntity.write(wrapper, material.material, TrimMaterial::write);
   }

   public MaybeMappedEntity<TrimMaterial> getMaterial() {
      return this.material;
   }

   public void setMaterial(MaybeMappedEntity<TrimMaterial> material) {
      this.material = material;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemProvidesTrimMaterial)) {
         return false;
      } else {
         ItemProvidesTrimMaterial that = (ItemProvidesTrimMaterial)obj;
         return this.material.equals(that.material);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.material);
   }
}
