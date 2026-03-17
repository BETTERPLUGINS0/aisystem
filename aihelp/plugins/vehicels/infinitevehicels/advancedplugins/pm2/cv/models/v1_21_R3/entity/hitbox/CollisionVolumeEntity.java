package advancedplugins.pm2.cv.models.v1_21_R3.entity.hitbox;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R3.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R3.entity.EntityConversionUtil;
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
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftLocation;
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

   public CollisionVolumeEntity(World world, @NotNull IJoint joint, @NotNull SubHitbox subHitbox) {
      super(EntityTypes.bd, var1);
      this.equippedItems = NonNullList.a(2, ItemStack.j);
      this.wornItems = NonNullList.a(4, ItemStack.j);
      this.attachmentPoint = var2;
      this.collisionBox = var3;
      this.configureEntityProperties();
   }

   private void configureEntityProperties() {
      this.n(true);
      this.f(true);
      this.ad = true;
   }

   @NotNull
   public EnumMainHand fy() {
      return EnumMainHand.b;
   }

   @NotNull
   public Iterable<ItemStack> fb() {
      return this.wornItems;
   }

   @NotNull
   public ItemStack a(EnumItemSlot slot) {
      ItemStack var10000;
      switch(var1.a()) {
      case a:
         var10000 = (ItemStack)this.equippedItems.get(var1.b());
         break;
      case b:
         var10000 = (ItemStack)this.wornItems.get(var1.b());
         break;
      case c:
         var10000 = ItemStack.j;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public void a(EnumItemSlot slot, @NotNull ItemStack stack) {
      switch(var1.a()) {
      case a:
         this.a(var1, (ItemStack)this.equippedItems.set(var1.b(), var2), var2);
         break;
      case b:
         this.a(var1, (ItemStack)this.wornItems.set(var1.b(), var2), var2);
      }

   }

   public boolean cp() {
      return true;
   }

   public void h(@NotNull Entity entity) {
   }

   @NotNull
   protected AxisAlignedBB c(Vec3D position) {
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

   public void h() {
      if (this.removalQueued) {
         this.at();
      } else {
         super.h();
         if (!this.validateEntityState()) {
            this.at();
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

   public boolean bi() {
      return true;
   }

   protected void o() {
   }

   protected void D(@NotNull Entity entity) {
   }

   public boolean bI() {
      return false;
   }

   public boolean a(@NotNull WorldServer world, @NotNull DamageSource source, float amount) {
      this.propagateDamageToBoundEntities(var1, var2, var3);
      return this.collisionBox.getDamageMultiplier() <= 1.0E-5F ? false : this.applyScaledDamage(var2, var3);
   }

   private void propagateDamageToBoundEntities(WorldServer world, DamageSource source, float amount) {
      Iterator var4 = this.collisionBox.getBoundEntities().values().iterator();

      while(var4.hasNext()) {
         org.bukkit.entity.Entity var5 = (org.bukkit.entity.Entity)var4.next();
         EntityConversionUtil.toNMS(var5).a(var1, var2, var3);
      }

   }

   private boolean applyScaledDamage(DamageSource source, float amount) {
      BaseEntity var3 = this.attachmentPoint.getVisualModel().getModeledEntity().getBase();
      CraftHumanEntity var4 = this.extractAttacker(var1);
      float var5 = var2 * this.collisionBox.getDamageMultiplier();
      return var3.hurt(this, var4, var1, var5);
   }

   private CraftHumanEntity extractAttacker(DamageSource source) {
      Entity var2 = var1.d();
      return var2 instanceof EntityHuman ? ((EntityHuman)var2).getBukkitEntity() : null;
   }

   @NotNull
   public EnumInteractionResult a(EntityHuman player, @NotNull EnumHand hand) {
      this.handleBoundEntityInteractions(var1, var2);
      return (EnumInteractionResult)(this.collisionBox.getDamageMultiplier() <= 1.0E-5F ? EnumInteractionResult.e : this.processMainEntityInteraction(var1, var2));
   }

   private void handleBoundEntityInteractions(EntityHuman player, EnumHand hand) {
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

   private EnumInteractionResult processMainEntityInteraction(EntityHuman player, EnumHand hand) {
      EquipmentSlot var3 = var2 == EnumHand.a ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
      EntityHandler.InteractionResult var4 = this.attachmentPoint.getVisualModel().getModeledEntity().getBase().interact(this, var1.getBukkitEntity(), var3);
      return this.mapInteractionResult(var4);
   }

   private EnumInteractionResult mapInteractionResult(EntityHandler.InteractionResult result) {
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

   public boolean dT() {
      return false;
   }

   @NotNull
   public CraftEntity getBukkitEntity() {
      CraftEntity var1 = this.retrieveBukkitEntity();
      if (var1 == null) {
         synchronized(this) {
            CraftLivingEntity var3 = new CraftLivingEntity(this.dV().getCraftServer(), this);
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

   private void storeBukkitEntity(CraftEntity craftEntity) {
      ReflectionUtils.set(this, ReflectionFieldCatalog.BUKKIT_WRAPPER, var1);
   }

   protected int l(int air) {
      return var1;
   }

   protected int m(int air) {
      return var1;
   }

   public void a(@NotNull RemovalReason reason) {
      super.a(var1);
      this.cleanupEntityReferences();
   }

   private void cleanupEntityReferences() {
      ModelAPI.setRenderCanceled(this.ar(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   public int getEntityId() {
      return this.ar();
   }

   public UUID getUniqueId() {
      return this.cG();
   }

   public void queueLocation(Vector3f location) {
      this.pendingPosition = var1;
   }

   public Location getLocation() {
      return CraftLocation.toBukkit(this.dt(), this.dV().getWorld(), this.getBukkitYaw(), this.dN());
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
   public void setOrientedVolume(CollisionVolumeEntity.OrientedBoundingVolume orientedVolume) {
      this.orientedVolume = var1;
   }

   @Generated
   public void setPendingPosition(Vector3f pendingPosition) {
      this.pendingPosition = var1;
   }

   @Generated
   public void setRemovalQueued(boolean removalQueued) {
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

   private static class OrientedBoundingVolume extends advancedplugins.pm2.cv.models.v1_21_R3.entity.hitbox.OrientedBoundingVolume {
      OrientedBoundingVolume(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Quaternionf rotation, float yaw) {
         super(var1, var3, var5, var7, var9, var11, var13, var14);
      }
   }
}
