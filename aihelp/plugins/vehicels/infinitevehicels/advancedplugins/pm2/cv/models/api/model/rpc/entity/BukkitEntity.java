package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.BukkitEntityData;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import advancedplugins.pm2.cv.models.api.nms.impl.DefaultBodyRotationController;
import advancedplugins.pm2.cv.models.api.nms.impl.EmptyLookController;
import advancedplugins.pm2.cv.models.api.nms.impl.EmptyMoveController;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class BukkitEntity implements BaseEntity<Entity> {
   protected final EntityHandler entityHandler = ModelAPI.getNMSHandler().getEntityHandler();
   protected final Entity original;
   protected final BukkitEntityData data;
   protected final BodyRotationController bodyRotationController;
   protected final MoveController moveController;
   protected final LookController lookController;
   protected boolean isVisible;

   public BukkitEntity(Entity var1) {
      this.original = var1;
      this.data = this.createEntityData(var1);
      this.bodyRotationController = this.entityHandler.wrapBodyRotationControl(var1, () -> {
         return new DefaultBodyRotationController(this);
      });
      this.moveController = this.entityHandler.wrapMoveController(var1, EmptyMoveController::new);
      this.lookController = this.entityHandler.wrapLookController(var1, EmptyLookController::new);
      this.entityHandler.wrapNavigation(var1);
   }

   protected BukkitEntityData createEntityData(Entity var1) {
      return new BukkitEntityData(var1);
   }

   public void registerData() {
      ModelAPI.getAPI().getDataTrackers().execute(this.getUUID(), (var1, var2) -> {
         var2.putEntityData(var1, this.data);
      });
   }

   public boolean isRemoved() {
      return this.entityHandler.isRemoved(this.original);
   }

   public boolean isAlive() {
      return this.data.isEntityValid();
   }

   public boolean isForcedAlive() {
      return this.data.isForcedAlive();
   }

   public void setForcedAlive(boolean var1) {
      this.data.setForcedAlive(var1);
   }

   public int getEntityId() {
      return this.original.getEntityId();
   }

   public UUID getUUID() {
      return this.original.getUniqueId();
   }

   public double getMaxStepHeight() {
      return this.entityHandler.getStepHeight(this.original);
   }

   public void setMaxStepHeight(double var1) {
      this.entityHandler.setStepHeight(this.original, var1);
   }

   public int getRenderRadius() {
      return this.data.getTracked().getBaseRange();
   }

   public void setRenderRadius(int var1) {
      this.data.getTracked().setBaseRange(var1);
   }

   public void setCollidableWith(Entity var1, boolean var2) {
      Entity var3 = this.original;
      if (var3 instanceof LivingEntity) {
         LivingEntity var4 = (LivingEntity)var3;
         Set var5 = var4.getCollidableExemptions();
         if (var2) {
            var5.remove(var1.getUniqueId());
         } else {
            var5.add(var1.getUniqueId());
         }
      }

   }

   public boolean isGlowing() {
      return this.original.isGlowing();
   }

   public int getGlowColor() {
      return ModelAPI.getEntityHandler().getGlowColor(this.original);
   }

   public boolean hurt(@Nullable HumanEntity var1, Object var2, float var3) {
      return this.entityHandler.hurt(this.original, var2, var3);
   }

   public boolean hurt(HitboxEntity var1, @Nullable HumanEntity var2, Object var3, float var4) {
      if (this.original.hasMetadata("skill-damage")) {
         List var16 = this.original.getMetadata("skill-damage");
         MetadataValue var6 = (MetadataValue)var16.get(0);
         this.original.setMetadata("skill-damage", var6);
         this.original.setMetadata("hitbox", new FixedMetadataValue(ModelAPI.PLUGIN, var1.getJoint().getJointId()));

         boolean var7;
         try {
            var7 = this.entityHandler.hurt(this.original, var3, var4);
         } finally {
            this.original.removeMetadata("hitbox", ModelAPI.PLUGIN);
            this.original.removeMetadata("skill-damage", var6.getOwningPlugin());
         }

         return var7;
      } else {
         this.original.setMetadata("hitbox", new FixedMetadataValue(ModelAPI.PLUGIN, var1.getJoint().getJointId()));

         boolean var5;
         try {
            var5 = this.entityHandler.hurt(this.original, var3, var4);
         } finally {
            this.original.removeMetadata("hitbox", ModelAPI.PLUGIN);
         }

         return var5;
      }
   }

   public EntityHandler.InteractionResult interact(HumanEntity var1, EquipmentSlot var2) {
      if (var1 instanceof Player) {
         Player var3 = (Player)var1;
         PlayerInteractAtEntityEvent var4 = new PlayerInteractAtEntityEvent(var3, this.original, new Vector(0, 0, 0), var2);
         Bukkit.getPluginManager().callEvent(var4);
         if (var4.isCancelled()) {
            return EntityHandler.InteractionResult.FAIL;
         }
      }

      return this.entityHandler.interact(this.original, var1, var2);
   }

   public EntityHandler.InteractionResult interact(HitboxEntity var1, HumanEntity var2, EquipmentSlot var3) {
      this.original.setMetadata("hitbox", new FixedMetadataValue(ModelAPI.PLUGIN, var1.getJoint().getJointId()));

      EntityHandler.InteractionResult var4;
      try {
         var4 = this.interact(var2, var3);
      } finally {
         this.original.removeMetadata("hitbox", ModelAPI.PLUGIN);
      }

      return var4;
   }

   public float getYRot() {
      return this.entityHandler.getYRot(this.original);
   }

   public float getYHeadRot() {
      return this.entityHandler.getYHeadRot(this.original);
   }

   public float getXHeadRot() {
      return this.entityHandler.getXHeadRot(this.original);
   }

   public float getYBodyRot() {
      return this.entityHandler.getYBodyRot(this.original);
   }

   public boolean isWalking() {
      return this.entityHandler.isWalking(this.original);
   }

   public boolean isStrafing() {
      return this.entityHandler.isStrafing(this.original);
   }

   public boolean isJumping() {
      return this.entityHandler.isJumping(this.original);
   }

   public boolean isFlying() {
      return this.entityHandler.isFlying(this.original);
   }

   public float getHealth() {
      return this.entityHandler.getHealth(this.original);
   }

   public float getMaxHealth() {
      return this.entityHandler.getMaxHealth(this.original);
   }

   public BoundingBox getBoundingBox() {
      return this.original.getBoundingBox();
   }

   public void save(SavedData var1) {
      BaseEntity.super.save(var1);
      Entity var2 = this.original;
      if (var2 instanceof LivingEntity) {
         LivingEntity var3 = (LivingEntity)var2;
         var1.putList("collide_exemption", var3.getCollidableExemptions());
      }

   }

   public void load(SavedData var1) {
      BaseEntity.super.load(var1);
      Entity var2 = this.original;
      if (var2 instanceof LivingEntity) {
         LivingEntity var3 = (LivingEntity)var2;
         var3.getCollidableExemptions().addAll(var1.getList("collide_exemption"));
      }

   }

   public Entity getOriginal() {
      return this.original;
   }

   public BukkitEntityData getData() {
      return this.data;
   }

   public BodyRotationController getBodyRotationController() {
      return this.bodyRotationController;
   }

   public MoveController getMoveController() {
      return this.moveController;
   }

   public LookController getLookController() {
      return this.lookController;
   }

   public boolean isVisible() {
      return this.isVisible;
   }

   public void setVisible(boolean var1) {
      this.isVisible = var1;
      Iterator var2;
      UUID var3;
      if (this.isVisible) {
         var2 = this.data.getTracking().keySet().iterator();

         while(var2.hasNext()) {
            var3 = (UUID)var2.next();
            this.entityHandler.forceSpawn(this, Bukkit.getPlayer(var3));
         }
      } else {
         var2 = this.data.getTracking().keySet().iterator();

         while(var2.hasNext()) {
            var3 = (UUID)var2.next();
            this.entityHandler.forceDespawn(this, Bukkit.getPlayer(var3));
         }
      }

   }
}
