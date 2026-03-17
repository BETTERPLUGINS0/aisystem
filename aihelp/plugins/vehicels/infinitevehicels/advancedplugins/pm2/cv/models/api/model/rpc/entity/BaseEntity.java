package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public interface BaseEntity<T> extends DataIO {
   T getOriginal();

   IEntityData getData();

   void registerData();

   boolean isVisible();

   void setVisible(boolean var1);

   boolean isRemoved();

   boolean isAlive();

   boolean isForcedAlive();

   void setForcedAlive(boolean var1);

   int getEntityId();

   UUID getUUID();

   default Location getLocation() {
      return this.getData().getLocation();
   }

   default List<Entity> getPassengers() {
      return this.getData().getPassengers();
   }

   double getMaxStepHeight();

   void setMaxStepHeight(double var1);

   int getRenderRadius();

   void setRenderRadius(int var1);

   void setCollidableWith(Entity var1, boolean var2);

   BodyRotationController getBodyRotationController();

   MoveController getMoveController();

   LookController getLookController();

   boolean isGlowing();

   int getGlowColor();

   boolean hurt(@Nullable HumanEntity var1, Object var2, float var3);

   boolean hurt(HitboxEntity var1, @Nullable HumanEntity var2, Object var3, float var4);

   EntityHandler.InteractionResult interact(HumanEntity var1, EquipmentSlot var2);

   EntityHandler.InteractionResult interact(HitboxEntity var1, HumanEntity var2, EquipmentSlot var3);

   float getYRot();

   float getYHeadRot();

   float getXHeadRot();

   float getYBodyRot();

   boolean isWalking();

   boolean isStrafing();

   boolean isJumping();

   boolean isFlying();

   float getHealth();

   float getMaxHealth();

   BoundingBox getBoundingBox();

   default void save(SavedData data) {
      data.putInt("render_radius", this.getRenderRadius());
      data.putDouble("step", this.getMaxStepHeight());
      this.getData().save().ifPresent((entityData) -> {
         data.putData("entity_data", entityData);
      });
      this.getBodyRotationController().save().ifPresent((controllerData) -> {
         data.putData("body_rotation", controllerData);
      });
   }

   default void load(SavedData data) {
      this.setRenderRadius(data.getInt("render_radius"));
      this.setMaxStepHeight(data.getDouble("step"));
      data.getData("entity_data").ifPresent((entityData) -> {
         this.getData().load(entityData);
      });
      data.getData("body_rotation").ifPresent((controllerData) -> {
         this.getBodyRotationController().load(controllerData);
      });
   }
}
