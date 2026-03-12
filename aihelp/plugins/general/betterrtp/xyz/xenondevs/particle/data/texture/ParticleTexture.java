package xyz.xenondevs.particle.data.texture;

import org.bukkit.Material;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

public class ParticleTexture extends ParticleData {
   private final Material material;
   private final byte data;

   ParticleTexture(Material material, byte data) {
      this.material = material;
      this.data = data;
   }

   public Material getMaterial() {
      return this.material;
   }

   public byte getData() {
      return this.data;
   }

   public Object toNMSData() {
      int id = this.getMaterial().getId();
      byte data = this.getData();
      return this.getEffect() == ParticleEffect.ITEM_CRACK ? new int[]{id, data} : new int[]{id | data << 12};
   }
}
