package advancedplugins.pm2.cv.models.v1_21_R4;

import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import lombok.Generated;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.control.ControllerMove;
import net.minecraft.world.level.World;

public enum NMSFields implements ReflectionUtils.ReflectionEnum {
   ENTITY_ENTITY_COUNTER(Entity.class, "c", "ENTITY_COUNTER"),
   ENTITY_dimensions(Entity.class, "bb", "dimensions"),
   ENTITY_eyeHeight(Entity.class, "bc", "eyeHeight"),
   ENTITY_bukkitEntity(Entity.class, "bukkitEntity", "bukkitEntity"),
   LIVING_ENTITY_noJumpDelay(EntityLiving.class, "ce", "noJumpDelay"),
   LIVING_ENTITY_jumping(EntityLiving.class, "bm", "jumping"),
   MOB_lookControl(EntityInsentient.class, "bO", "lookControl"),
   MOB_moveControl(EntityInsentient.class, "bP", "moveControl"),
   MOB_navigation(EntityInsentient.class, "bR", "navigation"),
   MOB_goalSelector(EntityInsentient.class, "bS", "goalSelector"),
   MOB_bodyRotationControl(EntityInsentient.class, "ca", "bodyRotationControl"),
   SERVER_GAME_PACKET_LISTENER_IMPL_clientIsFloating(PlayerConnection.class, "I", "clientIsFloating"),
   SERVER_COMMON_PACKET_LISTENER_IMPL_connection(ServerCommonPacketListenerImpl.class, "e", "connection"),
   MOVE_CONTROL_operation(ControllerMove.class, "k", "operation"),
   TRACKED_ENTITY_serverEntity(EntityTracker.class, "b", "serverEntity"),
   TRACKED_ENTITY_range(EntityTracker.class, "d", "range"),
   LEVEL_threadSafeRandom(World.class, "f", "threadSafeRandom");

   private final Class<?> target;
   private final String obfuscated;
   private final String mapped;

   public Class<?> target() {
      return this.getTarget();
   }

   @Generated
   private NMSFields(final Class<?> target, final String obfuscated, final String mapped) {
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
