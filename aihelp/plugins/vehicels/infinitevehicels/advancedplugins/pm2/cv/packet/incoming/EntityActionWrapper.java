package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.packet.PacketWrapper;

public class EntityActionWrapper extends PacketWrapper {
   private final int entityId;
   private final EntityActionWrapper.Action action;

   public EntityActionWrapper(int entityId, EntityActionWrapper.Action action) {
      this.entityId = var1;
      this.action = var2;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public EntityActionWrapper.Action getAction() {
      return this.action;
   }

   public static enum Action {
      START_SNEAKING,
      STOP_SNEAKING,
      LEAVE_BED,
      START_SPRINTING,
      STOP_SPRINTING,
      START_JUMP_HORSE,
      STOP_JUMP_HORSE,
      OPEN_VEHICLE_INVENTORY,
      START_FLYING_ELYTRA;

      // $FF: synthetic method
      private static EntityActionWrapper.Action[] $values() {
         return new EntityActionWrapper.Action[]{START_SNEAKING, STOP_SNEAKING, LEAVE_BED, START_SPRINTING, STOP_SPRINTING, START_JUMP_HORSE, STOP_JUMP_HORSE, OPEN_VEHICLE_INVENTORY, START_FLYING_ELYTRA};
      }
   }
}
