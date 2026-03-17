package advancedplugins.pm2.cv.models.v1_21_R7.entity.hitbox;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R7.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R7.entity.EntityConversionUtil;
import java.util.Iterator;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMainHand;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R6.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CollisionVolumeEntity extends EntityLiving implements HitboxEntity {
   private final NonNullList<ItemStack> equippedItems;
   private final NonNullList<ItemStack> wornItems;
   private final IJoint attachmentPoint;
   private final SubHitbox collisionBox;
   private CollisionVolumeEntity.OrientedBoundingVolume orientedVolume;
   private Vector3f pendingPosition;
   private boolean removalQueued;

   public CollisionVolumeEntity(World var1, @NotNull IJoint var2, @NotNull SubHitbox var3) {
      super(EntityTypes.bj, var1);
      this.equippedItems = NonNullList.a(2, ItemStack.l);
      this.wornItems = NonNullList.a(4, ItemStack.l);
      this.attachmentPoint = var2;
      this.collisionBox = var3;
      this.configureEntityProperties();
   }

   private void configureEntityProperties() {
      this.n(true);
      this.g(true);
      this.ar = true;
   }

   @NotNull
   public EnumMainHand as() {
      return EnumMainHand.b;
   }

   @NotNull
   public Iterable<ItemStack> getArmorSlots() {
      return this.wornItems;
   }

   @NotNull
   public ItemStack a(EnumItemSlot var1) {
      ItemStack var10000;
      switch(var1.a()) {
      case a:
         var10000 = (ItemStack)this.equippedItems.get(var1.b());
         break;
      case b:
         var10000 = (ItemStack)this.wornItems.get(var1.b());
         break;
      case c:
         var10000 = ItemStack.l;
         break;
      case d:
         var10000 = null;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public void a(EnumItemSlot var1, @NotNull ItemStack var2) {
      switch(var1.a()) {
      case a:
         this.a(var1, (ItemStack)this.equippedItems.set(var1.b(), var2), var2);
         break;
      case b:
         this.a(var1, (ItemStack)this.wornItems.set(var1.b(), var2), var2);
      }

   }

   public boolean cB() {
      return true;
   }

   public void h(@NotNull Entity var1) {
   }

   @NotNull
   protected AxisAlignedBB c(Vec3D var1) {
      if (this.collisionBox == null) {
         return super.c(var1);
      } else {
         return this.collisionBox.isOBB() ? this.createOrientedBounds() : this.createAxisAlignedBounds();
      }
   }

   private AxisAlignedBB createOrientedBounds() {
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

   private AxisAlignedBB createAxisAlignedBounds() {
      Vector3f var1 = this.collisionBox.getLocation();
      Vector3f var2 = this.collisionBox.getDimension();
      float var3 = var2.x * 0.5F;
      float var4 = var2.z * 0.5F;
      return new AxisAlignedBB((double)(var1.x - var3), (double)var1.y, (double)(var1.z - var4), (double)(var1.x + var3), (double)(var1.y + var2.y), (double)(var1.z + var4));
   }

   public void g() {
      if (this.removalQueued) {
         this.aB();
      } else {
         super.g();
         if (!this.validateEntityState()) {
            this.aB();
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
      Vec3D var1 = new Vec3D(this.pendingPosition);
      this.b(var1);
      Iterator var2 = this.collisionBox.getBoundEntities().values().iterator();

      while(var2.hasNext()) {
         org.bukkit.entity.Entity var3 = (org.bukkit.entity.Entity)var2.next();
         EntityConversionUtil.toNMS(var3).b(var1);
      }

   }

   public boolean bt() {
      return true;
   }

   protected void q() {
   }

   protected void E(@NotNull Entity var1) {
   }

   public boolean bU() {
      return false;
   }

   public boolean a(@NotNull WorldServer var1, @NotNull DamageSource var2, float var3) {
      this.propagateDamageToBoundEntities(var1, var2, var3);
      return this.collisionBox.getDamageMultiplier() <= 1.0E-5F ? false : this.applyScaledDamage(var2, var3);
   }

   private void propagateDamageToBoundEntities(WorldServer var1, DamageSource var2, float var3) {
      Iterator var4 = this.collisionBox.getBoundEntities().values().iterator();

      while(var4.hasNext()) {
         org.bukkit.entity.Entity var5 = (org.bukkit.entity.Entity)var4.next();
         EntityConversionUtil.toNMS(var5).a(var1, var2, var3);
      }

   }

   private boolean applyScaledDamage(DamageSource var1, float var2) {
      BaseEntity var3 = this.attachmentPoint.getVisualModel().getModeledEntity().getBase();
      CraftHumanEntity var4 = this.extractAttacker(var1);
      float var5 = var2 * this.collisionBox.getDamageMultiplier();
      return var3.hurt(this, var4, var1, var5);
   }

   private CraftHumanEntity extractAttacker(DamageSource var1) {
      Entity var2 = var1.d();
      return var2 instanceof EntityHuman ? ((EntityHuman)var2).getBukkitEntity() : null;
   }

   @NotNull
   public EnumInteractionResult a(EntityHuman var1, @NotNull EnumHand var2) {
      this.handleBoundEntityInteractions(var1, var2);
      return (EnumInteractionResult)(this.collisionBox.getDamageMultiplier() <= 1.0E-5F ? EnumInteractionResult.e : this.processMainEntityInteraction(var1, var2));
   }

   private void handleBoundEntityInteractions(EntityHuman var1, EnumHand var2) {
      CraftHumanEntity var3 = var1.getBukkitEntity();
      if (var3 instanceof Player) {
         Player var4 = (Player)var3;
         EquipmentSlot var5 = var2 == EnumHand.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;
         Iterator var6 = this.collisionBox.getBoundEntities().values().iterator();

         while(var6.hasNext()) {
            org.bukkit.entity.Entity var7 = (org.bukkit.entity.Entity)var6.next();
            PlayerInteractAtEntityEvent var8 = new PlayerInteractAtEntityEvent(var4, var7, new Vector(0, 0, 0), var5);
            Bukkit.getPluginManager().callEvent(var8);
            if (!var8.isCancelled()) {
               EntityConversionUtil.toNMS(var7).a(var1, var2);
            }
         }

      }
   }

   private EnumInteractionResult processMainEntityInteraction(EntityHuman var1, EnumHand var2) {
      EquipmentSlot var3 = var2 == EnumHand.a ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
      EntityHandler.InteractionResult var4 = this.attachmentPoint.getVisualModel().getModeledEntity().getBase().interact(this, var1.getBukkitEntity(), var3);
      return this.mapInteractionResult(var4);
   }

   private EnumInteractionResult mapInteractionResult(EntityHandler.InteractionResult var1) {
      Object var10000;
      switch(var1) {
      case SUCCESS:
         var10000 = EnumInteractionResult.b;
         break;
      case SUCCESS_NO_ITEM_USED:
         var10000 = EnumInteractionResult.f;
         break;
      case CONSUME:
         var10000 = EnumInteractionResult.c;
         break;
      case CONSUME_PARTIAL:
         var10000 = EnumInteractionResult.a;
         break;
      case PASS:
         var10000 = EnumInteractionResult.e;
         break;
      case FAIL:
         var10000 = EnumInteractionResult.d;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return (EnumInteractionResult)var10000;
   }

   public boolean ef() {
      return false;
   }

   @NotNull
   public CraftEntity getBukkitEntity() {
      CraftEntity var1 = this.retrieveBukkitEntity();
      if (var1 == null) {
         synchronized(this) {
            CraftLivingEntity var3 = new CraftLivingEntity(this.an().getCraftServer(), this);
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

   protected int l(int var1) {
      return var1;
   }

   protected int m(int var1) {
      return var1;
   }

   public void a(@NotNull RemovalReason var1) {
      super.a(var1);
      this.cleanupEntityReferences();
   }

   private void cleanupEntityReferences() {
      ModelAPI.setRenderCanceled(this.az(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   public int getEntityId() {
      return this.az();
   }

   public UUID getUniqueId() {
      return this.cT();
   }

   public void queueLocation(Vector3f var1) {
      this.pendingPosition = var1;
   }

   public Location getLocation() {
      return CraftLocation.toBukkit(this.dD(), this.an().getWorld(), this.getBukkitYaw(), this.dZ());
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

   private static class OrientedBoundingVolume extends advancedplugins.pm2.cv.models.v1_21_R7.entity.hitbox.OrientedBoundingVolume {
      OrientedBoundingVolume(double var1, double var3, double var5, double var7, double var9, double var11, Quaternionf var13, float var14) {
         super(var1, var3, var5, var7, var9, var11, var13, var14);
      }
   }
}
