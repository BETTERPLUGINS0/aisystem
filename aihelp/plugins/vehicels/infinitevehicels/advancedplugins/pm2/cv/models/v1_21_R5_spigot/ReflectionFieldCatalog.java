package advancedplugins.pm2.cv.models.v1_21_R5_spigot;

import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.control.ControllerMove;
import net.minecraft.world.level.World;

public enum ReflectionFieldCatalog implements ReflectionUtils.ReflectionEnum {
   COUNTER_FOR_ENTITIES(Entity.class, "c", "ENTITY_COUNTER"),
   ENTITY_SIZE_DATA(Entity.class, "by", "dimensions"),
   ENTITY_VIEW_HEIGHT(Entity.class, "bz", "eyeHeight"),
   BUKKIT_WRAPPER(Entity.class, "bukkitEntity", "bukkitEntity"),
   JUMP_COOLDOWN(EntityLiving.class, "cn", "noJumpDelay"),
   JUMP_STATUS(EntityLiving.class, "bB", "jumping"),
   VISUAL_CONTROLLER(EntityInsentient.class, "cd", "lookControl"),
   MOVEMENT_CONTROLLER(EntityInsentient.class, "ce", "moveControl"),
   PATHFINDING_SYSTEM(EntityInsentient.class, "cg", "navigation"),
   BEHAVIOR_MANAGER(EntityInsentient.class, "ch", "goalSelector"),
   ROTATION_CONTROLLER(EntityInsentient.class, "cp", "bodyRotationControl"),
   FLOATING_STATE(PlayerConnection.class, "I", "clientIsFloating"),
   NETWORK_CONNECTION(ServerCommonPacketListenerImpl.class, "e", "connection"),
   MOVEMENT_OPERATION(ControllerMove.class, "k", "operation"),
   TRACKED_SERVER_ENTITY(EntityTracker.class, "b", "serverEntity"),
   TRACKING_DISTANCE(EntityTracker.class, "d", "range"),
   SAFE_RANDOM_SOURCE(World.class, "f", "threadSafeRandom");

   private final Class<?> target;
   private final String obfuscated;
   private final String mapped;

   public Class<?> target() {
      return this.target;
   }

   public String getObfuscatedName() {
      return this.obfuscated;
   }

   public String getMappedName() {
      return this.mapped;
   }

   public boolean isForClass(Class<?> var1) {
      return this.target.equals(var1);
   }

   public static ReflectionFieldCatalog findByMappedName(String var0) {
      ReflectionFieldCatalog[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ReflectionFieldCatalog var4 = var1[var3];
         if (var4.getMappedName().equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   public static List<ReflectionFieldCatalog> getFieldsForClass(Class<?> var0) {
      ArrayList var1 = new ArrayList();
      ReflectionFieldCatalog[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ReflectionFieldCatalog var5 = var2[var4];
         if (var5.isForClass(var0)) {
            var1.add(var5);
         }
      }

      return var1;
   }

   @Generated
   private ReflectionFieldCatalog(final Class<?> param3, final String param4, final String param5) {
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
   private static ReflectionFieldCatalog[] $values() {
      return new ReflectionFieldCatalog[]{COUNTER_FOR_ENTITIES, ENTITY_SIZE_DATA, ENTITY_VIEW_HEIGHT, BUKKIT_WRAPPER, JUMP_COOLDOWN, JUMP_STATUS, VISUAL_CONTROLLER, MOVEMENT_CONTROLLER, PATHFINDING_SYSTEM, BEHAVIOR_MANAGER, ROTATION_CONTROLLER, FLOATING_STATE, NETWORK_CONNECTION, MOVEMENT_OPERATION, TRACKED_SERVER_ENTITY, TRACKING_DISTANCE, SAFE_RANDOM_SOURCE};
   }
}
