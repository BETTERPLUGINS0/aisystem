package xyz.xenondevs.particle.data;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class VibrationData extends ParticleData {
   private final Location start;
   private final Location blockDestination;
   private final Entity entitydestination;
   private final int ticks;

   public VibrationData(Location start, Location destination, int ticks) {
      this.start = (Location)Objects.requireNonNull(start);
      this.blockDestination = (Location)Objects.requireNonNull(destination);
      this.entitydestination = null;
      this.ticks = ticks;
   }

   public VibrationData(Location destination, int ticks) {
      this.start = null;
      this.blockDestination = (Location)Objects.requireNonNull(destination);
      this.entitydestination = null;
      this.ticks = ticks;
   }

   public VibrationData(Location start, Entity destination, int ticks) {
      this.start = (Location)Objects.requireNonNull(start);
      this.entitydestination = (Entity)Objects.requireNonNull(destination);
      this.blockDestination = null;
      this.ticks = ticks;
   }

   public VibrationData(Entity destination, int ticks) {
      this.start = null;
      this.entitydestination = (Entity)Objects.requireNonNull(destination);
      this.blockDestination = null;
      this.ticks = ticks;
   }

   public Location getStart() {
      return this.start;
   }

   public Location getBlockDestination() {
      return this.blockDestination;
   }

   public Entity getEntityDestination() {
      return this.entitydestination;
   }

   public int getTicks() {
      return this.ticks;
   }

   public Object toNMSData() {
      if (!(ReflectionUtils.MINECRAFT_VERSION < 17.0D) && this.getEffect() == ParticleEffect.VIBRATION) {
         boolean isBlockDest = this.blockDestination != null;
         Object start = ReflectionUtils.createBlockPosition(this.getStart());

         try {
            Object source;
            Object path;
            if (ReflectionUtils.MINECRAFT_VERSION < 19.0D) {
               if (isBlockDest) {
                  path = ReflectionUtils.createBlockPosition(this.getBlockDestination());
                  source = ParticleConstants.BLOCK_POSITION_SOURCE_CONSTRUCTOR.newInstance(path);
               } else {
                  source = ParticleConstants.ENTITY_POSITION_SOURCE_CONSTRUCTOR.newInstance(this.getEntityDestination().getEntityId());
               }

               path = ParticleConstants.VIBRATION_PATH_CONSTRUCTOR.newInstance(start, source, this.getTicks());
               return ParticleConstants.PARTICLE_PARAM_VIBRATION_CONSTRUCTOR.newInstance(path);
            } else {
               if (isBlockDest) {
                  path = ReflectionUtils.createBlockPosition(this.getBlockDestination());
                  source = ParticleConstants.BLOCK_POSITION_SOURCE_CONSTRUCTOR.newInstance(path);
               } else {
                  source = ParticleConstants.ENTITY_POSITION_SOURCE_CONSTRUCTOR.newInstance(ReflectionUtils.getEntityHandle(this.getEntityDestination()), 0.0F);
               }

               return ParticleConstants.PARTICLE_PARAM_VIBRATION_CONSTRUCTOR.newInstance(source, this.getTicks());
            }
         } catch (Exception var5) {
            return null;
         }
      } else {
         return null;
      }
   }
}
