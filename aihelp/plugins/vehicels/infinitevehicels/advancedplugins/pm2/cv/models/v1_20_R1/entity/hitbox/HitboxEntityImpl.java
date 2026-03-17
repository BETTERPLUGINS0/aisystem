package advancedplugins.pm2.cv.models.v1_20_R1.entity.hitbox;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_20_R1.NMSFields;
import advancedplugins.pm2.cv.models.v1_20_R1.entity.EntityUtils;
import java.util.Iterator;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.core.NonNullList;
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
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftLocation;
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
      super(EntityTypes.aI, var1);
      this.handItems = NonNullList.a(2, ItemStack.b);
      this.armorItems = NonNullList.a(4, ItemStack.b);
      this.joint = var2;
      this.subHitbox = var3;
      this.m(true);
      this.e(true);
      this.ae = true;
   }

   public EnumMainHand fh() {
      return EnumMainHand.b;
   }

   public Iterable<ItemStack> bJ() {
      return this.armorItems;
   }

   public ItemStack c(EnumItemSlot slot) {
      ItemStack var10000;
      switch(var1.a()) {
      case a:
         var10000 = (ItemStack)this.handItems.get(var1.b());
         break;
      case b:
         var10000 = (ItemStack)this.armorItems.get(var1.b());
         break;
      default:
         var10000 = ItemStack.b;
      }

      ItemStack var2 = var10000;
      return var2;
   }

   public void a(EnumItemSlot slot, ItemStack stack) {
      this.e(var2);
      switch(var1.a()) {
      case a:
         this.a(var1, (ItemStack)this.handItems.set(var1.b(), var2), var2);
         break;
      case b:
         this.a(var1, (ItemStack)this.armorItems.set(var1.b(), var2), var2);
      }

   }

   public boolean cb() {
      return true;
   }

   public void g(Entity entity) {
   }

   @NotNull
   protected AxisAlignedBB am() {
      if (this.subHitbox == null) {
         return super.am();
      } else {
         Vector3f var1 = this.subHitbox.getLocation();
         Vector3f var2 = this.subHitbox.getDimension();
         float var3;
         float var5;
         if (this.subHitbox.isOBB()) {
            var3 = var2.x * 0.5F;
            float var4 = var2.y * 0.5F;
            var5 = var2.z * 0.5F;
            Quaternionf var6 = this.subHitbox.getRotation();
            float var7 = this.subHitbox.getYaw();
            this.obb = new OBB((double)(var1.x - var3), (double)(var1.y - var4), (double)(var1.z - var5), (double)(var1.x + var3), (double)(var1.y + var5), (double)(var1.z + var5), var6, var7);
            return this.obb;
         } else {
            var3 = var2.x * 0.5F;
            var5 = var2.z * 0.5F;
            return new AxisAlignedBB((double)(var1.x - var3), (double)var1.y, (double)(var1.z - var5), (double)(var1.x + var3), (double)(var1.y + var2.y), (double)(var1.z + var5));
         }
      }
   }

   public void l() {
      if (this.markRemoved) {
         this.ai();
      } else {
         super.l();
         if (this.joint != null && this.subHitbox != null) {
            if (!this.joint.getVisualModel().getModeledEntity().getBase().isAlive()) {
               this.ai();
            } else if (this.location.isFinite()) {
               Vec3D var1 = new Vec3D(this.location);
               this.a(var1);
               Iterator var2 = this.subHitbox.getBoundEntities().values().iterator();

               while(var2.hasNext()) {
                  org.bukkit.entity.Entity var3 = (org.bukkit.entity.Entity)var2.next();
                  EntityUtils.nms(var3).a(var1);
               }
            }
         } else {
            this.ai();
         }
      }

   }

   public boolean aU() {
      return true;
   }

   protected void fd() {
   }

   protected void A(Entity entity) {
   }

   public boolean bp() {
      return false;
   }

   public boolean a(DamageSource source, float amount) {
      Iterator var3 = this.subHitbox.getBoundEntities().values().iterator();

      while(var3.hasNext()) {
         org.bukkit.entity.Entity var4 = (org.bukkit.entity.Entity)var3.next();
         EntityUtils.nms(var4).a(var1, var2);
      }

      if (this.subHitbox.getDamageMultiplier() <= 1.0E-5F) {
         return false;
      } else {
         Entity var7 = var1.d();
         CraftHumanEntity var5;
         if (var7 instanceof EntityHuman) {
            EntityHuman var6 = (EntityHuman)var7;
            var5 = var6.getBukkitEntity();
         } else {
            var5 = null;
         }

         return this.joint.getVisualModel().getModeledEntity().getBase().hurt(this, var5, var1, var2 * this.subHitbox.getDamageMultiplier());
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
         return EnumInteractionResult.d;
      } else {
         EntityHandler.InteractionResult var8 = this.joint.getVisualModel().getModeledEntity().getBase().interact(this, var1.getBukkitEntity(), var2 == EnumHand.a ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND);
         EnumInteractionResult var10000;
         switch(var8) {
         case SUCCESS:
            var10000 = EnumInteractionResult.a;
            break;
         case SUCCESS_NO_ITEM_USED:
            var10000 = EnumInteractionResult.a;
            break;
         case CONSUME:
            var10000 = EnumInteractionResult.b;
            break;
         case CONSUME_PARTIAL:
            var10000 = EnumInteractionResult.c;
            break;
         case PASS:
            var10000 = EnumInteractionResult.d;
            break;
         case FAIL:
            var10000 = EnumInteractionResult.e;
            break;
         default:
            throw new RuntimeException();
         }

         return var10000;
      }
   }

   public boolean dG() {
      return false;
   }

   public CraftEntity getBukkitEntity() {
      CraftEntity var1 = this.getBukkitEntityR();
      if (var1 == null) {
         synchronized(this) {
            CraftLivingEntity var3 = new CraftLivingEntity(this.dI().getCraftServer(), this);
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

   protected int m(int air) {
      return var1;
   }

   protected int n(int air) {
      return var1;
   }

   public void a(RemovalReason reason) {
      super.a(var1);
      ModelAPI.setRenderCanceled(this.af(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
   }

   public int getEntityId() {
      return this.af();
   }

   public UUID getUniqueId() {
      return this.ct();
   }

   public void queueLocation(Vector3f location) {
      this.location = var1;
   }

   public Location getLocation() {
      return CraftLocation.toBukkit(this.dg(), this.dI().getWorld(), this.getBukkitYaw(), this.dA());
   }

   @Nullable
   public OrientedBoundingBox getOrientedBoundingBox() {
      return this.obb == null ? null : this.obb.getBukkitOBB();
   }

   public void markRemoved() {
      this.markRemoved = true;
      ModelAPI.setRenderCanceled(this.af(), false);
      ModelAPI.getInteractionTracker().removeHitbox(this.getEntityId());
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
}
