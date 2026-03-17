package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleRepairConfiguration implements ConfigurationSectionWritable {
   private final Material repairMaterial;
   @Nullable
   private final String repairItemName;
   @Nullable
   private final List<String> repairItemLore;
   private final int customModelData;
   private final int repairAmount;
   private final int itemAmount;
   private final int cooldown;

   public static VehicleRepairConfiguration load(@NotNull ConfigurationSection var0) {
      String var1 = var0.getString("material");
      if (var1 == null) {
         throw new IllegalArgumentException("material cannot be null");
      } else {
         Material var2 = Material.matchMaterial(var1);
         if (var2 == null) {
            throw new IllegalArgumentException("material " + var1 + " is not a valid material");
         } else {
            String var3 = var0.getString("name");
            List var4 = var0.getStringList("lore");
            int var5 = var0.getInt("custom-model-data");
            int var6 = var0.getInt("repair-amount");
            int var7 = var0.getInt("item-amount", 1);
            int var8 = var0.getInt("cooldown", 5);
            return builder().repairMaterial(var2).repairItemName(var3).repairItemLore(var4).customModelData(var5).repairAmount(var6).itemAmount(var7).cooldown(var8).build();
         }
      }
   }

   public boolean takeOne(@NotNull Player var1) {
      Objects.requireNonNull(var1);
      ItemStack[] var2 = var1.getInventory().getContents();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         if (var5 != null && this.test(var5)) {
            var5.setAmount(var5.getAmount() - 1);
            var1.updateInventory();
            return true;
         }
      }

      return false;
   }

   public boolean test(@NotNull Player var1) {
      Objects.requireNonNull(var1);
      boolean var2 = false;
      ItemStack[] var3 = var1.getInventory().getContents();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack var6 = var3[var5];
         if (var6 != null && this.test(var6)) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   public boolean test(@NotNull ItemStack var1) {
      Objects.requireNonNull(var1);
      if (var1.getType() != this.repairMaterial) {
         return false;
      } else if (var1.getItemMeta() == null && (this.repairItemName != null || this.repairItemLore != null || this.customModelData > 0)) {
         return false;
      } else if (this.repairItemName != null && !var1.getItemMeta().getDisplayName().equals(this.repairItemName)) {
         return false;
      } else if (this.repairItemLore != null && !Objects.equals(var1.getItemMeta().getLore(), this.repairItemLore) && !this.repairItemLore.isEmpty()) {
         return false;
      } else if (this.customModelData > 0 && var1.getItemMeta().hasCustomModelData() && var1.getItemMeta().getCustomModelData() != this.customModelData) {
         return false;
      } else {
         return var1.getAmount() >= this.itemAmount;
      }
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("material", this.repairMaterial.name());
      var1.set("name", this.repairItemName);
      var1.set("lore", this.repairItemLore);
      var1.set("custom-model-data", this.customModelData);
      var1.set("repair-amount", this.repairAmount);
      var1.set("item-amount", this.itemAmount);
      var1.set("cooldown", this.cooldown);
   }

   public static VehicleRepairConfiguration.VehicleRepairConfigurationBuilder builder() {
      return new VehicleRepairConfiguration.VehicleRepairConfigurationBuilder();
   }

   public Material getRepairMaterial() {
      return this.repairMaterial;
   }

   @Nullable
   public String getRepairItemName() {
      return this.repairItemName;
   }

   @Nullable
   public List<String> getRepairItemLore() {
      return this.repairItemLore;
   }

   public int getCustomModelData() {
      return this.customModelData;
   }

   public int getRepairAmount() {
      return this.repairAmount;
   }

   public int getItemAmount() {
      return this.itemAmount;
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public VehicleRepairConfiguration(Material var1, @Nullable String var2, @Nullable List<String> var3, int var4, int var5, int var6, int var7) {
      this.repairMaterial = var1;
      this.repairItemName = var2;
      this.repairItemLore = var3;
      this.customModelData = var4;
      this.repairAmount = var5;
      this.itemAmount = var6;
      this.cooldown = var7;
   }

   public static class VehicleRepairConfigurationBuilder {
      private Material repairMaterial;
      private String repairItemName;
      private List<String> repairItemLore;
      private int customModelData;
      private int repairAmount;
      private int itemAmount;
      private int cooldown;

      VehicleRepairConfigurationBuilder() {
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder repairMaterial(Material var1) {
         this.repairMaterial = var1;
         return this;
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder repairItemName(@Nullable String var1) {
         this.repairItemName = var1;
         return this;
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder repairItemLore(@Nullable List<String> var1) {
         this.repairItemLore = var1;
         return this;
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder customModelData(int var1) {
         this.customModelData = var1;
         return this;
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder repairAmount(int var1) {
         this.repairAmount = var1;
         return this;
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder itemAmount(int var1) {
         this.itemAmount = var1;
         return this;
      }

      public VehicleRepairConfiguration.VehicleRepairConfigurationBuilder cooldown(int var1) {
         this.cooldown = var1;
         return this;
      }

      public VehicleRepairConfiguration build() {
         return new VehicleRepairConfiguration(this.repairMaterial, this.repairItemName, this.repairItemLore, this.customModelData, this.repairAmount, this.itemAmount, this.cooldown);
      }

      public String toString() {
         String var10000 = String.valueOf(this.repairMaterial);
         return "VehicleRepairConfiguration.VehicleRepairConfigurationBuilder(repairMaterial=" + var10000 + ", repairItemName=" + this.repairItemName + ", repairItemLore=" + String.valueOf(this.repairItemLore) + ", customModelData=" + this.customModelData + ", repairAmount=" + this.repairAmount + ", itemAmount=" + this.itemAmount + ", cooldown=" + this.cooldown + ")";
      }
   }
}
