package github.nighter.smartspawner.spawner.lootgen.loot;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public record LootItem(Material material, int minAmount, int maxAmount, double chance, Integer minDurability, Integer maxDurability, PotionType potionType, double sellPrice) {
   public LootItem(Material material, int minAmount, int maxAmount, double chance, Integer minDurability, Integer maxDurability, PotionType potionType, double sellPrice) {
      this.material = material;
      this.minAmount = minAmount;
      this.maxAmount = maxAmount;
      this.chance = chance;
      this.minDurability = minDurability;
      this.maxDurability = maxDurability;
      this.potionType = potionType;
      this.sellPrice = sellPrice;
   }

   public ItemStack createItemStack(Random random) {
      if (this.material == null) {
         return null;
      } else {
         ItemStack item = new ItemStack(this.material, 1);
         if (this.minDurability != null && this.maxDurability != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
               int durability = random.nextInt(this.maxDurability - this.minDurability + 1) + this.minDurability;
               ((Damageable)meta).setDamage(durability);
               item.setItemMeta(meta);
            }
         }

         if (this.material == Material.TIPPED_ARROW && this.potionType != null) {
            PotionMeta meta = (PotionMeta)item.getItemMeta();
            if (meta != null) {
               meta.setBasePotionType(this.potionType);
               item.setItemMeta(meta);
            }
         }

         return item;
      }
   }

   public int generateAmount(Random random) {
      return random.nextInt(this.maxAmount - this.minAmount + 1) + this.minAmount;
   }

   public boolean isAvailable() {
      return this.material != null;
   }

   public Material material() {
      return this.material;
   }

   public int minAmount() {
      return this.minAmount;
   }

   public int maxAmount() {
      return this.maxAmount;
   }

   public double chance() {
      return this.chance;
   }

   public Integer minDurability() {
      return this.minDurability;
   }

   public Integer maxDurability() {
      return this.maxDurability;
   }

   public PotionType potionType() {
      return this.potionType;
   }

   public double sellPrice() {
      return this.sellPrice;
   }
}
