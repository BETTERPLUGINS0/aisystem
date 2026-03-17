package advancedplugins.pm2.cv.models.api.model.rpc.joint.type;

import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface HeldItem {
   ItemStack EMPTY = new ItemStack(Material.AIR);

   Vector3f getLocation();

   Quaternionf getRotation();

   ItemDisplayTransform getDisplay();

   void setDisplay(ItemDisplayTransform var1);

   HeldItem.ItemStackSupplier getItemProvider();

   void setItemProvider(Supplier<ItemStack> var1);

   void setItemProvider(HeldItem.ItemStackSupplier var1);

   void clearItemProvider();

   ItemStack getItem();

   public static class TemporaryItemStackSupplier implements HeldItem.ItemStackSupplier {
      private final Supplier<ItemStack> stackSupplier;

      public TemporaryItemStackSupplier(Supplier<ItemStack> var1) {
         this.stackSupplier = var1;
      }

      public ItemStack supply() {
         return (ItemStack)this.stackSupplier.get();
      }

      public void save(SavedData var1) {
      }

      public void load(SavedData var1) {
      }
   }

   public static class StaticItemStackSupplier implements HeldItem.ItemStackSupplier {
      private ItemStack itemStack;

      public ItemStack supply() {
         return this.itemStack;
      }

      public void save(SavedData var1) {
         var1.putString("type", "static");
         var1.putItemStack("item", this.itemStack);
      }

      public void load(SavedData var1) {
         this.itemStack = var1.getItemStack("item", HeldItem.EMPTY);
      }

      @Generated
      public ItemStack getItemStack() {
         return this.itemStack;
      }

      @Generated
      public void setItemStack(ItemStack var1) {
         this.itemStack = var1;
      }
   }

   public static class EquipmentSupplier implements HeldItem.ItemStackSupplier {
      private LivingEntity target;
      private EquipmentSlot slot;

      public ItemStack supply() {
         if (this.target != null && this.slot != null) {
            EntityEquipment var1 = this.target.getEquipment();
            return var1 == null ? HeldItem.EMPTY : var1.getItem(this.slot);
         } else {
            return HeldItem.EMPTY;
         }
      }

      public void save(SavedData var1) {
         var1.putString("type", "equipment");
         var1.putUUID("target", this.target.getUniqueId());
         var1.put("slot", this.slot.name());
      }

      public void load(SavedData var1) {
         UUID var2 = var1.getUUID("target");
         String var3 = var1.getString("slot");
         if (var2 != null && var3 != null) {
            Entity var4 = Bukkit.getEntity(var2);
            if (var4 instanceof LivingEntity) {
               LivingEntity var5 = (LivingEntity)var4;

               try {
                  this.slot = EquipmentSlot.valueOf(var3);
               } catch (Throwable var7) {
                  LogUtil.error(1, "Failed to load EquipmentSupplier: Invalid slot " + var3 + ".");
                  return;
               }

               this.target = var5;
            } else {
               LogUtil.error(1, "Failed to load EquipmentSupplier: Target entity does not exist or is not LivingEntity.");
            }
         }

      }

      @Generated
      public LivingEntity getTarget() {
         return this.target;
      }

      @Generated
      public EquipmentSlot getSlot() {
         return this.slot;
      }

      @Generated
      public void setTarget(LivingEntity var1) {
         this.target = var1;
      }

      @Generated
      public void setSlot(EquipmentSlot var1) {
         this.slot = var1;
      }
   }

   public interface ItemStackSupplier extends DataIO {
      ItemStack supply();
   }
}
