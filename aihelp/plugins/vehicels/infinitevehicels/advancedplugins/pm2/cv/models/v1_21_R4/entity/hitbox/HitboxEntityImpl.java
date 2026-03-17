package advancedplugins.pm2.cv.models.v1_21_R4.entity.hitbox;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R4.NMSFields;
import advancedplugins.pm2.cv.models.v1_21_R4.entity.EntityUtils;
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
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HitboxEntityImpl extends EntityLiving implements HitboxEntity {
   private final NonNullList<ItemStack> handItems;
   private final NonNullList<ItemStack> armorItems;
   private final IJoint joint;
   private final SubHitbox subHitbox;
   private OBB obb;
   private Vector3f location;
   private boolean markRemoved;

   public HitboxEntityImpl(World world, @NotNull IJoint joint, @NotNull SubHitbox subHitbox) {
      super(EntityTypes.bf, var1);
      this.handItems = NonNullList.a(2, ItemStack.l);
      this.armorItems = NonNullList.a(4, ItemStack.l);
      this.joint = var2;
      this.subHitbox = var3;
      this.m(true);
      this.f(true);
      this.ad = true;
   }

   @NotNull
   public EnumMainHand fy() {
      return EnumMainHand.b;
   }

   @NotNull
   public Iterable<ItemStack> getArmorSlots() {
      return this.armorItems;
   }

   @NotNull
   public ItemStack a(EnumItemSlot slot) {
      ItemStack var2;
      switch(var1.a()) {
      case a:
         var2 = (ItemStack)this.handItems.get(var1.b());
         break;
      case b:
         var2 = (ItemStack)this.armorItems.get(var1.b());
         break;
      case c:
         var2 = ItemStack.l;
         break;
      default:
         throw new RuntimeException((String)null, (Throwable)null);
      }

      return var2;
   }

   public void a(EnumItemSlot slot, @NotNull ItemStack stack) {
      switch(var1.a()) {
      case a:
         this.a(var1, (ItemStack)this.handItems.set(var1.b(), var2), var2);
         break;
      case b:
         this.a(var1, (ItemStack)this.armorItems.set(var1.b(), var2), var2);
      }

   }

   public boolean co() {
      return true;
   }

   public void h(@NotNull Entity entity) {
   }

   @NotNull
   protected AxisAlignedBB c(Vec3D position) {
      if (this.subHitbox == null) {
         return super.c(var1);
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
            return new AxisAlignedBB((double)(var2.x - var4), (double)var2.y, (double)(var2.z - var6), (double)(var2.x + var4), (double)(var2.y + var3.y), (double)(var2.z + var6));
         }
      }
   }

   public void g() {
      if (this.markRemoved) {
         this.aq();
      } else {
         super.g();
         if (this.joint != null && this.subHitbox != null) {
            if (!this.joint.getVisualModel().getModeledEntity().getBase().isAlive()) {
               this.aq();
            } else if (this.location.isFinite()) {
               Vec3D var1 = new Vec3D(this.location);
               this.b(var1);
               Iterator var2 = this.subHitbox.getBoundEntities().values().iterator();

               while(var2.hasNext()) {
                  org.bukkit.entity.Entity var3 = (org.bukkit.entity.Entity)var2.next();
                  EntityUtils.nms(var3).b(var1);
               }
            }
         } else {
            this.aq();
         }
      }

   }

   public boolean bh() {
      return true;
   }

   protected void n() {
   }

   protected void D(@NotNull Entity entity) {
   }

   public boolean bG() {
      return false;
   }

   public boolean a(@NotNull WorldServer world, @NotNull DamageSource source, float amount) {
      Iterator var4 = this.subHitbox.getBoundEntities().values().iterator();

      while(var4.hasNext()) {
         org.bukkit.entity.Entity var5 = (org.bukkit.entity.Entity)var4.next();
         EntityUtils.nms(var5).a(var1, var2, var3);
      }

      if (this.subHitbox.getDamageMultiplier() <= 1.0E-5F) {
         return false;
      } else {
         BaseEntity var9 = this.joint.getVisualModel().getModeledEntity().getBase();
         Entity var6 = var2.d();
         CraftHumanEntity var7;
         if (var6 instanceof EntityHuman) {
            EntityHuman var8 = (EntityHuman)var6;
            var7 = var8.getBukkitEntity();
         } else {
            var7 = null;
         }

         return var9.hurt(this, var7, var2, var3 * this.subHitbox.getDamageMultiplier());
      }
   }

   @NotNull
   public EnumInteractionResult a(EntityHuman player, @NotNull EnumHand hand) {
      CraftHumanEntity var3 = var1.getBukkitEntity();
      if (var3 instanceof Player) {
         Player var4 = (Player)var3;
         Iterator var5 = this.subHitbox.getBoundEntities().values().iterator();

         while(var5.hasNext()) {
            org.bukkit.entity.Entity var6 = (org.bukkit.entity.Entity)var5.next();
            PlayerInteractAtEntityEvent var7 = new PlayerInteractAtEntityEvent(var4, var6, new Vector(0, 0, 0), var2 == EnumHand.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(var7);
            if (!var7.isCancelled()) {
               EntityUtils.nms(var6).a(var1, var2);
            }
         }
      }

      if (this.subHitbox.getDamageMultiplier() <= 1.0E-5F) {
         return EnumInteractionResult.e;
      } else {
         EntityHandler.InteractionResult var8 = this.joint.getVisualModel().getModeledEntity().getBase().interact(this, var1.getBukkitEntity(), var2 == EnumHand.a ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND);
         Object var9;
         switch(var8) {
         case SUCCESS:
            var9 = EnumInteractionResult.b;
            break;
         case SUCCESS_NO_ITEM_USED:
            var9 = EnumInteractionResult.f;
            break;
         case CONSUME:
            var9 = EnumInteractionResult.c;
            break;
         case CONSUME_PARTIAL:
            var9 = EnumInteractionResult.a;
            break;
         case PASS:
            var9 = EnumInteractionResult.e;
            break;
         case FAIL:
            var9 = EnumInteractionResult.d;
            break;
         default:
            throw new RuntimeException((String)null, (Throwable)null);
         }

         return (EnumInteractionResult)var9;
      }
   }

   public boolean dT() {
      return false;
   }

   @NotNull
   public CraftEntity getBukkitEntity() {
      CraftEntity var1 = this.getBukkitEntityR();
      if (var1 == null) {
         synchronized(this) {
            CraftLivingEntity var3 = new CraftLivingEntity(this.dV().getCraftServer(), this);
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

   private void setBukkitEntityR(CraftEntity craftEntity) {
      ReflectionUtils.set(this, NMSFields.ENTITY_bukkitEntity, var1);
   }

   protected int l(int air) {
      return var1;
   }

   protected int m(int air) {
      return var1;
   }

   public void a(@NotNull RemovalReason reason) {
      super.a(var1);
      ModelAPI.setRenderCanceled(this.ao(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   public int getEntityId() {
      return this.ao();
   }

   public UUID getUniqueId() {
      return this.cG();
   }

   public void queueLocation(Vector3f location) {
      this.location = var1;
   }

   public Location getLocation() {
      return CraftLocation.toBukkit(this.dt(), this.dV().getWorld(), this.getBukkitYaw(), this.dN());
   }

   @Nullable
   public OrientedBoundingBox getOrientedBoundingBox() {
      return this.obb == null ? null : this.obb.getBukkitOBB();
   }

   public void markRemoved() {
      this.markRemoved = true;
      ModelAPI.setRenderCanceled(this.ao(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   @Generated
   public void setObb(OBB obb) {
      this.obb = var1;
   }

   @Generated
   public void setLocation(Vector3f location) {
      this.location = var1;
   }

   @Generated
   public void setMarkRemoved(boolean markRemoved) {
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
