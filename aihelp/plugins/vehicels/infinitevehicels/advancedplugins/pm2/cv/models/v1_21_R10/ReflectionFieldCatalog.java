package advancedplugins.pm2.cv.models.v1_21_R10;

import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;

public enum ReflectionFieldCatalog implements ReflectionUtils.ReflectionEnum {
   COUNTER_FOR_ENTITIES(Entity.class, "c", "ENTITY_COUNTER"),
   ENTITY_SIZE_DATA(Entity.class, "bz", "dimensions"),
   ENTITY_VIEW_HEIGHT(Entity.class, "bA", "eyeHeight"),
   BUKKIT_WRAPPER(Entity.class, "bukkitEntity", "bukkitEntity"),
   JUMP_COOLDOWN(LivingEntity.class, "cu", "noJumpDelay"),
   JUMP_STATUS(LivingEntity.class, "bL", "jumping"),
   VISUAL_CONTROLLER(Mob.class, "cm", "lookControl"),
   MOVEMENT_CONTROLLER(Mob.class, "cn", "moveControl"),
   PATHFINDING_SYSTEM(Mob.class, "cp", "navigation"),
   BEHAVIOR_MANAGER(Mob.class, "cq", "goalSelector"),
   ROTATION_CONTROLLER(Mob.class, "cy", "bodyRotationControl"),
   FLOATING_STATE(ServerGamePacketListenerImpl.class, "I", "clientIsFloating"),
   NETWORK_CONNECTION(ServerCommonPacketListenerImpl.class, "e", "connection"),
   MOVEMENT_OPERATION(MoveControl.class, "k", "operation"),
   TRACKED_SERVER_ENTITY(TrackedEntity.class, "b", "serverEntity"),
   TRACKING_DISTANCE(TrackedEntity.class, "d", "range"),
   SAFE_RANDOM_SOURCE(Level.class, "C", "threadSafeRandom");

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
