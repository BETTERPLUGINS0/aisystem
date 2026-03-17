package advancedplugins.pm2.cv.models.v1_21_R5.entity.hitbox;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.NMSFields;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityUtils;
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
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HitboxEntityImpl extends LivingEntity implements HitboxEntity {
   private final NonNullList<ItemStack> handItems;
   private final NonNullList<ItemStack> armorItems;
   private final IJoint joint;
   private final SubHitbox subHitbox;
   private OBB obb;
   private Vector3f location;
   private boolean markRemoved;

   public HitboxEntityImpl(Level var1, @NotNull IJoint var2, @NotNull SubHitbox var3) {
      super(EntityType.SILVERFISH, var1);
      this.handItems = NonNullList.withSize(2, ItemStack.EMPTY);
      this.armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
      this.joint = var2;
      this.subHitbox = var3;
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
      return this.armorItems;
   }

   @NotNull
   public ItemStack getItemBySlot(EquipmentSlot var1) {
      ItemStack var2;
      switch(var1.getType()) {
      case HAND:
         var2 = (ItemStack)this.handItems.get(var1.getIndex());
         break;
      case HUMANOID_ARMOR:
         var2 = (ItemStack)this.armorItems.get(var1.getIndex());
         break;
      case ANIMAL_ARMOR:
         var2 = ItemStack.EMPTY;
         break;
      default:
         throw new RuntimeException((String)null, (Throwable)null);
      }

      return var2;
   }

   public void setItemSlot(EquipmentSlot var1, @NotNull ItemStack var2) {
      switch(var1.getType()) {
      case HAND:
         this.onEquipItem(var1, (ItemStack)this.handItems.set(var1.getIndex(), var2), var2);
         break;
      case HUMANOID_ARMOR:
         this.onEquipItem(var1, (ItemStack)this.armorItems.set(var1.getIndex(), var2), var2);
      }

   }

   public boolean isInvisible() {
      return true;
   }

   public void push(@NotNull Entity var1) {
   }

   @NotNull
   protected AABB makeBoundingBox(Vec3 var1) {
      if (this.subHitbox == null) {
         return super.makeBoundingBox(var1);
      } else {
         Vector3f var2 = this.subHitbox.getLocation();
         Vector3f var3 = this.subHitbox.getDimension();
         float var4;
         float var6;
         if (this.subHitbox.isOBB()) {
            var4 = var3.x * 0.5F;
            var6 = var3.y * 0.5F;
            var6 = var3.z * 0.5F;
            Quaternionf var7 = this.subHitbox.getRotation();
            float var8 = this.subHitbox.getYaw();
            this.obb = new OBB((double)(var2.x - var4), (double)(var2.y - var6), (double)(var2.z - var6), (double)(var2.x + var4), (double)(var2.y + var6), (double)(var2.z + var6), var7, var8);
            return this.obb;
         } else {
            var4 = var3.x * 0.5F;
            var6 = var3.z * 0.5F;
            return new AABB((double)(var2.x - var4), (double)var2.y, (double)(var2.z - var6), (double)(var2.x + var4), (double)(var2.y + var3.y), (double)(var2.z + var6));
         }
      }
   }

   public void tick() {
      if (this.markRemoved) {
         this.discard();
      } else {
         super.tick();
         if (this.joint != null && this.subHitbox != null) {
            if (!this.joint.getVisualModel().getModeledEntity().getBase().isAlive()) {
               this.discard();
            } else if (this.location.isFinite()) {
               Vec3 var1 = new Vec3(this.location);
               this.setPos(var1);
               Iterator var2 = this.subHitbox.getBoundEntities().values().iterator();

               while(var2.hasNext()) {
                  org.bukkit.entity.Entity var3 = (org.bukkit.entity.Entity)var2.next();
                  EntityUtils.nms(var3).setPos(var1);
               }
            }
         } else {
            this.discard();
         }
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
      Iterator var4 = this.subHitbox.getBoundEntities().values().iterator();

      while(var4.hasNext()) {
         org.bukkit.entity.Entity var5 = (org.bukkit.entity.Entity)var4.next();
         EntityUtils.nms(var5).hurtServer(var1, var2, var3);
      }

      if (this.subHitbox.getDamageMultiplier() <= 1.0E-5F) {
         return false;
      } else {
         BaseEntity var9 = this.joint.getVisualModel().getModeledEntity().getBase();
         Entity var6 = var2.getEntity();
         CraftHumanEntity var7;
         if (var6 instanceof Player) {
            Player var8 = (Player)var6;
            var7 = var8.getBukkitEntity();
         } else {
            var7 = null;
         }

         return var9.hurt(this, var7, var2, var3 * this.subHitbox.getDamageMultiplier());
      }
   }

   @NotNull
   public InteractionResult interact(Player var1, @NotNull InteractionHand var2) {
      CraftHumanEntity var3 = var1.getBukkitEntity();
      if (var3 instanceof org.bukkit.entity.Player) {
         org.bukkit.entity.Player var4 = (org.bukkit.entity.Player)var3;
         Iterator var5 = this.subHitbox.getBoundEntities().values().iterator();

         while(var5.hasNext()) {
            org.bukkit.entity.Entity var6 = (org.bukkit.entity.Entity)var5.next();
            PlayerInteractAtEntityEvent var7 = new PlayerInteractAtEntityEvent(var4, var6, new Vector(0, 0, 0), var2 == InteractionHand.OFF_HAND ? org.bukkit.inventory.EquipmentSlot.OFF_HAND : org.bukkit.inventory.EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(var7);
            if (!var7.isCancelled()) {
               EntityUtils.nms(var6).interact(var1, var2);
            }
         }
      }

      if (this.subHitbox.getDamageMultiplier() <= 1.0E-5F) {
         return InteractionResult.PASS;
      } else {
         EntityHandler.InteractionResult var8 = this.joint.getVisualModel().getModeledEntity().getBase().interact(this, var1.getBukkitEntity(), var2 == InteractionHand.MAIN_HAND ? org.bukkit.inventory.EquipmentSlot.HAND : org.bukkit.inventory.EquipmentSlot.OFF_HAND);
         Object var9;
         switch(var8) {
         case SUCCESS:
            var9 = InteractionResult.SUCCESS_SERVER;
            break;
         case SUCCESS_NO_ITEM_USED:
            var9 = InteractionResult.TRY_WITH_EMPTY_HAND;
            break;
         case CONSUME:
            var9 = InteractionResult.CONSUME;
            break;
         case CONSUME_PARTIAL:
            var9 = InteractionResult.SUCCESS;
            break;
         case PASS:
            var9 = InteractionResult.PASS;
            break;
         case FAIL:
            var9 = InteractionResult.FAIL;
            break;
         default:
            throw new RuntimeException((String)null, (Throwable)null);
         }

         return (InteractionResult)var9;
      }
   }

   public boolean shouldBeSaved() {
      return false;
   }

   @NotNull
   public CraftEntity getBukkitEntity() {
      CraftEntity var1 = this.getBukkitEntityR();
      if (var1 == null) {
         synchronized(this) {
            CraftLivingEntity var3 = new CraftLivingEntity(this.level().getCraftServer(), this);
            this.setBukkitEntityR(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   private CraftEntity getBukkitEntityR() {
      return (CraftEntity)ReflectionUtils.get(this, NMSFields.ENTITY_bukkitEntity);
   }

   private void setBukkitEntityR(CraftEntity var1) {
      ReflectionUtils.set(this, NMSFields.ENTITY_bukkitEntity, var1);
   }

   protected int decreaseAirSupply(int var1) {
      return var1;
   }

   protected int increaseAirSupply(int var1) {
      return var1;
   }

   public void remove(@NotNull RemovalReason var1) {
      super.remove(var1);
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
      this.location = var1;
   }

   public Location getLocation() {
      return CraftLocation.toBukkit(this.position(), this.level().getWorld(), this.getBukkitYaw(), this.getXRot());
   }

   @Nullable
   public OrientedBoundingBox getOrientedBoundingBox() {
      return this.obb == null ? null : this.obb.getBukkitOBB();
   }

   public void markRemoved() {
      this.markRemoved = true;
      ModelAPI.setRenderCanceled(this.getId(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   @Generated
   public void setObb(OBB var1) {
      this.obb = var1;
   }

   @Generated
   public void setLocation(Vector3f var1) {
      this.location = var1;
   }

   @Generated
   public void setMarkRemoved(boolean var1) {
      this.markRemoved = var1;
   }

   @Generated
   public NonNullList<ItemStack> getHandItems() {
      return this.handItems;
   }

   @Generated
   public NonNullList<ItemStack> getArmorItems() {
      return this.armorItems;
   }

   @Generated
   public IJoint getJoint() {
      return this.joint;
   }

   @Generated
   public SubHitbox getSubHitbox() {
      return this.subHitbox;
   }

   @Generated
   public OBB getObb() {
      return this.obb;
   }

   @Generated
   public boolean isMarkRemoved() {
      return this.markRemoved;
   }
}
