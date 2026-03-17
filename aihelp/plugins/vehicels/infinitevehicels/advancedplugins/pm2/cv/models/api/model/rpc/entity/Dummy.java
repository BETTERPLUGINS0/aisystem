package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.DummyEntityData;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import advancedplugins.pm2.cv.models.api.nms.impl.DefaultBodyRotationController;
import advancedplugins.pm2.cv.models.api.nms.impl.EmptyLookController;
import advancedplugins.pm2.cv.models.api.nms.impl.EmptyMoveController;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public class Dummy<T> implements BaseEntity<T> {
   protected final int entityId;
   protected final UUID uuid;
   protected final T original;
   protected final DummyEntityData<T> data;
   protected final BodyRotationController bodyRotationController;
   protected final MoveController moveController;
   protected final LookController lookController;
   protected boolean detectingPlayers;
   protected boolean isRemoved;
   protected boolean isWalking;
   protected boolean isStrafing;
   protected boolean isJumping;
   protected boolean isFlying;
   protected boolean isGlowing;
   protected int glowColor;
   protected float yHeadRot;
   protected float xHeadRot;
   protected float yBodyRot;
   protected BoundingBox boundingBox;

   public Dummy() {
      this((Object)null);
   }

   public Dummy(T var1) {
      this(UUID.randomUUID(), var1);
   }

   public Dummy(UUID var1, T var2) {
      this(ModelAPI.getEntityHandler().getNextEntityId(), var1, var2);
   }

   public Dummy(int var1, UUID var2, T var3) {
      this.detectingPlayers = true;
      this.boundingBox = new BoundingBox();
      this.entityId = var1;
      this.uuid = var2;
      this.original = var3;
      this.data = new DummyEntityData(this);
      this.bodyRotationController = new DefaultBodyRotationController(this);
      this.moveController = new EmptyMoveController();
      this.lookController = new EmptyLookController();
   }

   public boolean isAlive() {
      return !this.isRemoved;
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public int getRenderRadius() {
      return this.data.getRenderRadius();
   }

   public void setRenderRadius(int var1) {
      this.data.setRenderRadius(var1);
   }

   public float getYRot() {
      return this.yHeadRot;
   }

   public float getHealth() {
      return 20.0F;
   }

   public float getMaxHealth() {
      return 20.0F;
   }

   public void setLocation(Location var1) {
      this.data.setLocation(var1);
   }

   public void syncLocation(Location var1) {
      this.data.setLocation(var1);
      this.setYBodyRot(var1.getYaw());
      this.setYHeadRot(var1.getYaw());
      this.setXHeadRot(var1.getPitch());
   }

   public void setForceViewing(Player var1, boolean var2) {
      if (var2) {
         this.setForceHidden(var1, false);
         this.data.getTracked().addForcedPairing(var1.getUniqueId());
      } else {
         this.data.getTracked().removeForcedPairing(var1.getUniqueId());
      }

   }

   public void setForceHidden(Player var1, boolean var2) {
      if (var2) {
         this.setForceViewing(var1, false);
         this.data.getTracked().addForcedHidden(var1.getUniqueId());
      } else {
         this.data.getTracked().removeForcedHidden(var1.getUniqueId());
      }

   }

   public void registerData() {
      ModelAPI.getAPI().getDataTrackers().execute(this.getUUID(), (var1, var2) -> {
         var2.putEntityData(var1, this.data);
      });
   }

   public boolean isVisible() {
      return true;
   }

   public void setVisible(boolean var1) {
   }

   public boolean isForcedAlive() {
      return false;
   }

   public void setForcedAlive(boolean var1) {
   }

   public double getMaxStepHeight() {
      return 0.0D;
   }

   public void setMaxStepHeight(double var1) {
   }

   public void setCollidableWith(Entity var1, boolean var2) {
   }

   public boolean hurt(@Nullable HumanEntity var1, Object var2, float var3) {
      return false;
   }

   public boolean hurt(HitboxEntity var1, @Nullable HumanEntity var2, Object var3, float var4) {
      return false;
   }

   public EntityHandler.InteractionResult interact(HumanEntity var1, EquipmentSlot var2) {
      return EntityHandler.InteractionResult.SUCCESS_NO_ITEM_USED;
   }

   public EntityHandler.InteractionResult interact(HitboxEntity var1, HumanEntity var2, EquipmentSlot var3) {
      return EntityHandler.InteractionResult.SUCCESS_NO_ITEM_USED;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public T getOriginal() {
      return this.original;
   }

   public DummyEntityData<T> getData() {
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

   public boolean isDetectingPlayers() {
      return this.detectingPlayers;
   }

   public void setDetectingPlayers(boolean var1) {
      this.detectingPlayers = var1;
   }

   public boolean isRemoved() {
      return this.isRemoved;
   }

   public void setRemoved(boolean var1) {
      this.isRemoved = var1;
   }

   public boolean isWalking() {
      return this.isWalking;
   }

   public void setWalking(boolean var1) {
      this.isWalking = var1;
   }

   public boolean isStrafing() {
      return this.isStrafing;
   }

   public void setStrafing(boolean var1) {
      this.isStrafing = var1;
   }

   public boolean isJumping() {
      return this.isJumping;
   }

   public void setJumping(boolean var1) {
      this.isJumping = var1;
   }

   public boolean isFlying() {
      return this.isFlying;
   }

   public void setFlying(boolean var1) {
      this.isFlying = var1;
   }

   public boolean isGlowing() {
      return this.isGlowing;
   }

   public void setGlowing(boolean var1) {
      this.isGlowing = var1;
   }

   public int getGlowColor() {
      return this.glowColor;
   }

   public void setGlowColor(int var1) {
      this.glowColor = var1;
   }

   public float getYHeadRot() {
      return this.yHeadRot;
   }

   public void setYHeadRot(float var1) {
      this.yHeadRot = var1;
   }

   public float getXHeadRot() {
      return this.xHeadRot;
   }

   public void setXHeadRot(float var1) {
      this.xHeadRot = var1;
   }

   public float getYBodyRot() {
      return this.yBodyRot;
   }

   public void setYBodyRot(float var1) {
      this.yBodyRot = var1;
   }

   public BoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   public void setBoundingBox(BoundingBox var1) {
      this.boundingBox = var1;
   }
}
