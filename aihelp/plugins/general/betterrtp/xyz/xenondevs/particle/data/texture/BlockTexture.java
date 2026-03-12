package xyz.xenondevs.particle.data.texture;

import java.lang.reflect.Field;
import org.bukkit.Material;
import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.PropertyType;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class BlockTexture extends ParticleTexture {
   public BlockTexture(Material material) {
      super(material, (byte)0);
   }

   public BlockTexture(Material material, byte data) {
      super(material, data);
   }

   public Object toNMSData() {
      if (this.getMaterial() != null && this.getMaterial().isBlock() && this.getEffect() != null && this.getEffect().hasProperty(PropertyType.REQUIRES_BLOCK)) {
         if (ReflectionUtils.MINECRAFT_VERSION < 13.0D) {
            return super.toNMSData();
         } else {
            Object block = this.getBlockData(this.getMaterial());
            if (block == null) {
               return null;
            } else {
               try {
                  return ParticleConstants.PARTICLE_PARAM_BLOCK_CONSTRUCTOR.newInstance(this.getEffect().getNMSObject(), block);
               } catch (Exception var3) {
                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   public Object getBlockData(Material material) {
      try {
         Object block;
         if (ReflectionUtils.MINECRAFT_VERSION < 17.0D) {
            Field blockField = ReflectionUtils.getFieldOrNull(ParticleConstants.BLOCKS_CLASS, material.name(), false);
            if (blockField == null) {
               return null;
            }

            block = ReflectionUtils.readField(blockField, (Object)null);
         } else {
            block = ParticleConstants.REGISTRY_GET_METHOD.invoke(ParticleConstants.BLOCK_REGISTRY, ReflectionUtils.getMinecraftKey(material.name().toLowerCase()));
         }

         return ParticleConstants.BLOCK_GET_BLOCK_DATA_METHOD.invoke(block);
      } catch (Exception var4) {
         return null;
      }
   }
}
