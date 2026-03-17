package advancedplugins.pm2.cv.models.v1_21_R10.entity.hitbox;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R10.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.EntityConversionUtil;
import java.util.Iterator;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftLocation;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CollisionVolumeEntity extends LivingEntity implements HitboxEntity {
   private final NonNullList<ItemStack> equippedItems;
   private final NonNullList<ItemStack> wornItems;
   private final IJoint attachmentPoint;
   private final SubHitbox collisionBox;
   private CollisionVolumeEntity.OrientedBoundingVolume orientedVolume;
   private Vector3f pendingPosition;
   private boolean removalQueued;

   public CollisionVolumeEntity(Level var1, @NotNull IJoint var2, @NotNull SubHitbox var3) {
      super(EntityType.SILVERFISH, var1);
      this.equippedItems = NonNullList.withSize(2, ItemStack.EMPTY);
      this.wornItems = NonNullList.withSize(4, ItemStack.EMPTY);
      this.attachmentPoint = var2;
      this.collisionBox = var3;
      this.configureEntityProperties();
   }

   private void configureEntityProperties() {
      this.setInvulnerable(true);
      this.setNoGravity(true);
      this.noPhysics = true;
   }

   @NotNull
   public HumanoidArm getMainArm() {
      return HumanoidArm.RIGHT;
   }

   @NotNull
   public Iterable<ItemStack> getArmorSlots() {
      return this.wornItems;
   }

   @NotNull
   public ItemStack getItemBySlot(EquipmentSlot var1) {
      ItemStack var10000;
      switch(var1.getType()) {
      case HAND:
         var10000 = (ItemStack)this.equippedItems.get(var1.getIndex());
         break;
      case HUMANOID_ARMOR:
         var10000 = (ItemStack)this.wornItems.get(var1.getIndex());
         break;
      case ANIMAL_ARMOR:
         var10000 = ItemStack.EMPTY;
         break;
      case SADDLE:
         var10000 = null;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public void setItemSlot(EquipmentSlot var1, @NotNull ItemStack var2) {
      switch(var1.getType()) {
      case HAND:
         this.onEquipItem(var1, (ItemStack)this.equippedItems.set(var1.getIndex(), var2), var2);
         break;
      case HUMANOID_ARMOR:
         this.onEquipItem(var1, (ItemStack)this.wornItems.set(var1.getIndex(), var2), var2);
      }

   }

   public boolean isInvisible() {
      return true;
   }

   public void push(@NotNull Entity var1) {
   }

   @NotNull
   protected AABB makeBoundingBox(Vec3 var1) {
      if (this.collisionBox == null) {
         return super.makeBoundingBox(var1);
      } else {
         return this.collisionBox.isOBB() ? this.createOrientedBounds() : this.createAxisAlignedBounds();
      }
   }

   private AABB createOrientedBounds() {
      Vector3f var1 = this.collisionBox.getLocation();
      Vector3f var2 = this.collisionBox.getDimension();
      float var3 = var2.x * 0.5F;
      float var4 = var2.y * 0.5F;
      float var5 = var2.z * 0.5F;
      Quaternionf var6 = this.collisionBox.getRotation();
      float var7 = this.collisionBox.getYaw();
      this.orientedVolume = new CollisionVolumeEntity.OrientedBoundingVolume((double)(var1.x - var3), (double)(var1.y - var4), (double)(var1.z - var5), (double)(var1.x + var3), (double)(var1.y + var4), (double)(var1.z + var5), var6, var7);
      return this.orientedVolume;
   }

   private AABB createAxisAlignedBounds() {
      Vector3f var1 = this.collisionBox.getLocation();
      Vector3f var2 = this.collisionBox.getDimension();
      float var3 = var2.x * 0.5F;
      float var4 = var2.z * 0.5F;
      return new AABB((double)(var1.x - var3), (double)var1.y, (double)(var1.z - var4), (double)(var1.x + var3), (double)(var1.y + var2.y), (double)(var1.z + var4));
   }

   public void tick() {
      if (this.removalQueued) {
         this.discard();
      } else {
         super.tick();
         if (!this.validateEntityState()) {
            this.discard();
         } else {
            if (this.pendingPosition.isFinite()) {
               this.updatePositions();
            }

         }
      }
   }

   private boolean validateEntityState() {
      return this.attachmentPoint != null && this.collisionBox != null && this.attachmentPoint.getVisualModel().getModeledEntity().getBase().isAlive();
   }

   private void updatePositions() {
      Vec3 var1 = new Vec3(this.pendingPosition);
      this.setPos(var1);
      Iterator var2 = this.collisionBox.getBoundEntities().values().iterator();

      while(var2.hasNext()) {
         org.bukkit.entity.Entity var3 = (org.bukkit.entity.Entity)var2.next();
         EntityConversionUtil.toNMS(var3).setPos(var1);
      }

   }

   public boolean fireImmune() {
      return true;
   }

   protected void pushEntities() {
   }

   protected void doPush(@NotNull Entity var1) {
   }

   public boolean isPushable() {
      return false;
   }

   public boolean hurtServer(@NotNull ServerLevel var1, @NotNull DamageSource var2, float var3) {
      this.propagateDamageToBoundEntities(var1, var2, var3);
      return this.collisionBox.getDamageMultiplier() <= 1.0E-5F ? false : this.applyScaledDamage(var2, var3);
   }

   private void propagateDamageToBoundEntities(ServerLevel var1, DamageSource var2, float var3) {
      Iterator var4 = this.collisionBox.getBoundEntities().values().iterator();

      while(var4.hasNext()) {
         org.bukkit.entity.Entity var5 = (org.bukkit.entity.Entity)var4.next();
         EntityConversionUtil.toNMS(var5).hurtServer(var1, var2, var3);
      }

   }

   private boolean applyScaledDamage(DamageSource var1, float var2) {
      BaseEntity var3 = this.attachmentPoint.getVisualModel().getModeledEntity().getBase();
      CraftHumanEntity var4 = this.extractAttacker(var1);
      float var5 = var2 * this.collisionBox.getDamageMultiplier();
      return var3.hurt(this, var4, var1, var5);
   }

   private CraftHumanEntity extractAttacker(DamageSource var1) {
      Entity var2 = var1.getEntity();
      return var2 instanceof Player ? ((Player)var2).getBukkitEntity() : null;
   }

   @NotNull
   public InteractionResult interact(Player var1, @NotNull InteractionHand var2) {
      this.handleBoundEntityInteractions(var1, var2);
      return (InteractionResult)(this.collisionBox.getDamageMultiplier() <= 1.0E-5F ? InteractionResult.PASS : this.processMainEntityInteraction(var1, var2));
   }

   private void handleBoundEntityInteractions(Player var1, InteractionHand var2) {
      CraftHumanEntity var3 = var1.getBukkitEntity();
      if (var3 instanceof org.bukkit.entity.Player) {
         org.bukkit.entity.Player var4 = (org.bukkit.entity.Player)var3;
         org.bukkit.inventory.EquipmentSlot var5 = var2 == InteractionHand.OFF_HAND ? org.bukkit.inventory.EquipmentSlot.OFF_HAND : org.bukkit.inventory.EquipmentSlot.HAND;
         Iterator var6 = this.collisionBox.getBoundEntities().values().iterator();

         while(var6.hasNext()) {
            org.bukkit.entity.Entity var7 = (org.bukkit.entity.Entity)var6.next();
            PlayerInteractAtEntityEvent var8 = new PlayerInteractAtEntityEvent(var4, var7, new Vector(0, 0, 0), var5);
            Bukkit.getPluginManager().callEvent(var8);
            if (!var8.isCancelled()) {
               EntityConversionUtil.toNMS(var7).interact(var1, var2);
            }
         }

      }
   }

   private InteractionResult processMainEntityInteraction(Player var1, InteractionHand var2) {
      org.bukkit.inventory.EquipmentSlot var3 = var2 == InteractionHand.MAIN_HAND ? org.bukkit.inventory.EquipmentSlot.HAND : org.bukkit.inventory.EquipmentSlot.OFF_HAND;
      EntityHandler.InteractionResult var4 = this.attachmentPoint.getVisualModel().getModeledEntity().getBase().interact(this, var1.getBukkitEntity(), var3);
      return this.mapInteractionResult(var4);
   }

   private InteractionResult mapInteractionResult(EntityHandler.InteractionResult var1) {
      Object var10000;
      switch(var1) {
      case SUCCESS:
         var10000 = InteractionResult.SUCCESS_SERVER;
         break;
      case SUCCESS_NO_ITEM_USED:
         var10000 = InteractionResult.TRY_WITH_EMPTY_HAND;
         break;
      case CONSUME:
         var10000 = InteractionResult.CONSUME;
         break;
      case CONSUME_PARTIAL:
         var10000 = InteractionResult.SUCCESS;
         break;
      case PASS:
         var10000 = InteractionResult.PASS;
         break;
      case FAIL:
         var10000 = InteractionResult.FAIL;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return (InteractionResult)var10000;
   }

   public boolean shouldBeSaved() {
      return false;
   }

   @NotNull
   public CraftEntity getBukkitEntity() {
      CraftEntity var1 = this.retrieveBukkitEntity();
      if (var1 == null) {
         synchronized(this) {
            CraftLivingEntity var3 = new CraftLivingEntity(this.level().getCraftServer(), this);
            this.storeBukkitEntity(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   private CraftEntity retrieveBukkitEntity() {
      return (CraftEntity)ReflectionUtils.get(this, ReflectionFieldCatalog.BUKKIT_WRAPPER);
   }

   private void storeBukkitEntity(CraftEntity var1) {
      ReflectionUtils.set(this, ReflectionFieldCatalog.BUKKIT_WRAPPER, var1);
   }

   protected int decreaseAirSupply(int var1) {
      return var1;
   }

   protected int increaseAirSupply(int var1) {
      return var1;
   }

   public void remove(@NotNull RemovalReason var1) {
      super.remove(var1);
      this.cleanupEntityReferences();
   }

   private void cleanupEntityReferences() {
      ModelAPI.setRenderCanceled(this.getId(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   public int getEntityId() {
      return this.getId();
   }

   public UUID getUniqueId() {
      return this.getUUID();
   }

   public void queueLocation(Vector3f var1) {
      this.pendingPosition = var1;
   }

   public Location getLocation() {
      return CraftLocation.toBukkit(this.position(), this.level().getWorld(), this.getBukkitYaw(), this.getXRot());
   }

   @Nullable
   public OrientedBoundingBox getOrientedBoundingBox() {
      return this.orientedVolume == null ? null : this.orientedVolume.getBukkitOBB();
   }

   public void markRemoved() {
      this.removalQueued = true;
      this.cleanupEntityReferences();
   }

   public IJoint getJoint() {
      return this.attachmentPoint;
   }

   public SubHitbox getSubHitbox() {
      return this.collisionBox;
   }

   @Generated
   public void setOrientedVolume(CollisionVolumeEntity.OrientedBoundingVolume var1) {
      this.orientedVolume = var1;
   }

   @Generated
   public void setPendingPosition(Vector3f var1) {
      this.pendingPosition = var1;
   }

   @Generated
   public void setRemovalQueued(boolean var1) {
      this.removalQueued = var1;
   }

   @Generated
   public NonNullList<ItemStack> getEquippedItems() {
      return this.equippedItems;
   }

   @Generated
   public NonNullList<ItemStack> getWornItems() {
      return this.wornItems;
   }

   @Generated
   public IJoint getAttachmentPoint() {
      return this.attachmentPoint;
   }

   @Generated
   public SubHitbox getCollisionBox() {
      return this.collisionBox;
   }

   @Generated
   public CollisionVolumeEntity.OrientedBoundingVolume getOrientedVolume() {
      return this.orientedVolume;
   }

   @Generated
   public Vector3f getPendingPosition() {
      return this.pendingPosition;
   }

   @Generated
   public boolean isRemovalQueued() {
      return this.removalQueued;
   }

   private static class OrientedBoundingVolume extends advancedplugins.pm2.cv.models.v1_21_R10.entity.hitbox.OrientedBoundingVolume {
      OrientedBoundingVolume(double var1, double var3, double var5, double var7, double var9, double var11, Quaternionf var13, float var14) {
         super(var1, var3, var5, var7, var9, var11, var13, var14);
      }
   }
}
