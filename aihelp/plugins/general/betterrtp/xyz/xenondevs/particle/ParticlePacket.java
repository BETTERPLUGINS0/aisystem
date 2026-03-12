package xyz.xenondevs.particle;

import java.lang.reflect.Constructor;
import org.bukkit.Location;
import org.bukkit.Material;
import xyz.xenondevs.particle.data.ParticleData;
import xyz.xenondevs.particle.data.SculkChargeData;
import xyz.xenondevs.particle.data.ShriekData;
import xyz.xenondevs.particle.data.VibrationData;
import xyz.xenondevs.particle.data.color.DustData;
import xyz.xenondevs.particle.data.color.NoteColor;
import xyz.xenondevs.particle.data.color.ParticleColor;
import xyz.xenondevs.particle.data.color.RegularColor;
import xyz.xenondevs.particle.data.texture.BlockTexture;
import xyz.xenondevs.particle.data.texture.ItemTexture;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class ParticlePacket {
   private final ParticleEffect particle;
   private final float offsetX;
   private final float offsetY;
   private final float offsetZ;
   private final float speed;
   private final int amount;
   private final ParticleData particleData;

   public ParticlePacket(ParticleEffect particle, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData particleData) {
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.offsetZ = offsetZ;
      this.speed = speed;
      this.amount = amount;
      if (ReflectionUtils.MINECRAFT_VERSION > 17.0D) {
         if (particle == ParticleEffect.BARRIER) {
            this.particle = ParticleEffect.BLOCK_MARKER;
            this.particleData = new BlockTexture(Material.BARRIER);
            this.particleData.setEffect(ParticleEffect.BLOCK_MARKER);
            return;
         }

         if (particle == ParticleEffect.LIGHT) {
            this.particle = ParticleEffect.BLOCK_MARKER;
            this.particleData = new BlockTexture(Material.LIGHT);
            this.particleData.setEffect(ParticleEffect.BLOCK_MARKER);
            return;
         }
      }

      this.particle = particle;
      this.particleData = particleData;
   }

   public ParticlePacket(ParticleEffect particle, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
      this(particle, offsetX, offsetY, offsetZ, speed, amount, (ParticleData)null);
   }

   public ParticleEffect getParticle() {
      return this.particle;
   }

   public float getOffsetX() {
      return this.offsetX;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public float getOffsetZ() {
      return this.offsetZ;
   }

   public float getSpeed() {
      return this.speed;
   }

   public int getAmount() {
      return this.amount;
   }

   public ParticleData getParticleData() {
      return this.particleData;
   }

   public Object createPacket(Location location) {
      try {
         ParticleEffect effect = this.getParticle();
         ParticleData data = this.getParticleData();
         double version = ReflectionUtils.MINECRAFT_VERSION;
         if (effect == null || effect.getFieldName().equals("NONE")) {
            return null;
         }

         if (data != null) {
            if (data.getEffect() != effect) {
               return null;
            }

            Object nmsData = data.toNMSData();
            if (nmsData == null) {
               return null;
            }

            if ((!(data instanceof DustData) || !(version >= 13.0D)) && (!(data instanceof VibrationData) || !(version >= 17.0D)) && (!(data instanceof ShriekData) || !(version >= 19.0D)) && (!(data instanceof SculkChargeData) || !(version >= 19.0D)) && (!(data instanceof RegularColor) || !(version >= 17.0D) || !effect.hasProperty(PropertyType.DUST))) {
               if (data instanceof BlockTexture && effect.hasProperty(PropertyType.REQUIRES_BLOCK) || data instanceof ItemTexture && effect.hasProperty(PropertyType.REQUIRES_ITEM)) {
                  return this.createTexturedParticlePacket(location, nmsData);
               }

               if (data instanceof ParticleColor && effect.hasProperty(PropertyType.COLORABLE)) {
                  return this.createColoredParticlePacket(location, nmsData);
               }

               return null;
            }

            return this.createGenericParticlePacket(location, nmsData);
         }

         if (!effect.hasProperty(PropertyType.REQUIRES_BLOCK) && !effect.hasProperty(PropertyType.REQUIRES_ITEM)) {
            return this.createPacket(effect.getNMSObject(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), new int[0]);
         }
      } catch (Exception var7) {
      }

      return null;
   }

   private Object createGenericParticlePacket(Location location, Object param) {
      return this.createPacket(param, (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), new int[0]);
   }

   private Object createTexturedParticlePacket(Location location, Object param) {
      ParticleEffect effect = this.getParticle();
      double version = ReflectionUtils.MINECRAFT_VERSION;
      return this.createPacket(version < 13.0D ? effect.getNMSObject() : param, (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), version < 13.0D ? (int[])param : new int[0]);
   }

   private Object createColoredParticlePacket(Location location, Object param) {
      ParticleEffect effect = this.getParticle();
      ParticleData data = this.getParticleData();
      if (data instanceof NoteColor && effect.equals(ParticleEffect.NOTE)) {
         return this.createPacket(effect.getNMSObject(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), ((NoteColor)data).getRed(), 0.0F, 0.0F, this.getSpeed(), this.getAmount(), new int[0]);
      } else if (data instanceof RegularColor) {
         RegularColor color = (RegularColor)data;
         return !(ReflectionUtils.MINECRAFT_VERSION < 13.0D) && effect.equals(ParticleEffect.REDSTONE) ? this.createPacket(param, (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), new int[0]) : this.createPacket(effect.getNMSObject(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), effect.equals(ParticleEffect.REDSTONE) && color.getRed() == 0.0F ? 1.17549435E-38F : color.getRed(), color.getGreen(), color.getBlue(), 1.0F, 0, new int[0]);
      } else {
         return null;
      }
   }

   private Object createPacket(Object param, float locationX, float locationY, float locationZ, float offsetX, float offsetY, float offsetZ, float speed, int amount, int[] data) {
      Constructor packetConstructor = ParticleConstants.PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;

      try {
         if (ReflectionUtils.MINECRAFT_VERSION < 13.0D) {
            return packetConstructor.newInstance(param, true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount, data);
         } else {
            return ReflectionUtils.MINECRAFT_VERSION < 15.0D ? packetConstructor.newInstance(param, true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount) : packetConstructor.newInstance(param, true, (double)locationX, (double)locationY, (double)locationZ, offsetX, offsetY, offsetZ, speed, amount);
         }
      } catch (Exception var13) {
         return null;
      }
   }
}
