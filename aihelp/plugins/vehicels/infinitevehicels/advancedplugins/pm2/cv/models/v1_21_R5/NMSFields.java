package advancedplugins.pm2.cv.models.v1_21_R5;

import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import lombok.Generated;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;

public enum NMSFields implements ReflectionUtils.ReflectionEnum {
   ENTITY_ENTITY_COUNTER(Entity.class, "c", "ENTITY_COUNTER"),
   ENTITY_dimensions(Entity.class, "by", "dimensions"),
   ENTITY_eyeHeight(Entity.class, "bz", "eyeHeight"),
   ENTITY_bukkitEntity(Entity.class, "bukkitEntity", "bukkitEntity"),
   LIVING_ENTITY_noJumpDelay(LivingEntity.class, "cn", "noJumpDelay"),
   LIVING_ENTITY_jumping(LivingEntity.class, "bB", "jumping"),
   MOB_lookControl(Mob.class, "cd", "lookControl"),
   MOB_moveControl(Mob.class, "ce", "moveControl"),
   MOB_navigation(Mob.class, "cg", "navigation"),
   MOB_goalSelector(Mob.class, "ch", "goalSelector"),
   MOB_bodyRotationControl(Mob.class, "cp", "bodyRotationControl"),
   SERVER_GAME_PACKET_LISTENER_IMPL_clientIsFloating(ServerGamePacketListenerImpl.class, "I", "clientIsFloating"),
   SERVER_COMMON_PACKET_LISTENER_IMPL_connection(ServerCommonPacketListenerImpl.class, "e", "connection"),
   MOVE_CONTROL_operation(MoveControl.class, "k", "operation"),
   TRACKED_ENTITY_serverEntity(TrackedEntity.class, "b", "serverEntity"),
   TRACKED_ENTITY_range(TrackedEntity.class, "d", "range"),
   LEVEL_threadSafeRandom(Level.class, "f", "threadSafeRandom");

   private final Class<?> target;
   private final String obfuscated;
   private final String mapped;

   public Class<?> target() {
      return this.getTarget();
   }

   @Generated
   private NMSFields(final Class<?> param3, final String param4, final String param5) {
      this.target = var3;
      this.obfuscated = var4;
      this.mapped = var5;
   }

   @Generated
   public Class<?> getTarget() {
      return this.target;
   }

   @Generated
   public String getObfuscated() {
      return this.obfuscated;
   }

   @Generated
   public String getMapped() {
      return this.mapped;
   }

   // $FF: synthetic method
   private static NMSFields[] $values() {
      return new NMSFields[]{ENTITY_ENTITY_COUNTER, ENTITY_dimensions, ENTITY_eyeHeight, ENTITY_bukkitEntity, LIVING_ENTITY_noJumpDelay, LIVING_ENTITY_jumping, MOB_lookControl, MOB_moveControl, MOB_navigation, MOB_goalSelector, MOB_bodyRotationControl, SERVER_GAME_PACKET_LISTENER_IMPL_clientIsFloating, SERVER_COMMON_PACKET_LISTENER_IMPL_connection, MOVE_CONTROL_operation, TRACKED_ENTITY_serverEntity, TRACKED_ENTITY_range, LEVEL_threadSafeRandom};
   }
}
