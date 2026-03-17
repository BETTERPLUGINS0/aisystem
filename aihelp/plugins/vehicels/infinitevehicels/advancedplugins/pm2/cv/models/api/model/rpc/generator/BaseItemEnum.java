package advancedplugins.pm2.cv.models.api.model.rpc.generator;

import java.util.function.BiConsumer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;

public enum BaseItemEnum {
   FILLED_MAP(BaseItemEnum.Cons.MAP_CONSUMER, Material.FILLED_MAP),
   LEATHER_BOOTS(BaseItemEnum.Cons.LEATHER_CONSUMER, Material.LEATHER_BOOTS),
   LEATHER_CHESTPLATE(BaseItemEnum.Cons.LEATHER_CONSUMER, Material.LEATHER_CHESTPLATE),
   LEATHER_HORSE_ARMOR(BaseItemEnum.Cons.LEATHER_CONSUMER, Material.LEATHER_HORSE_ARMOR),
   LEATHER_LEGGINGS(BaseItemEnum.Cons.LEATHER_CONSUMER, Material.LEATHER_LEGGINGS),
   LINGERING_POTION(BaseItemEnum.Cons.POTION_CONSUMER, Material.LINGERING_POTION),
   POTION(BaseItemEnum.Cons.POTION_CONSUMER, Material.POTION),
   SPLASH_POTION(BaseItemEnum.Cons.POTION_CONSUMER, Material.SPLASH_POTION),
   TIPPED_ARROW(BaseItemEnum.Cons.POTION_CONSUMER, Material.TIPPED_ARROW);

   private final BiConsumer<ItemMeta, Color> metaConsumer;
   private final Material material;

   private BaseItemEnum(BiConsumer<ItemMeta, Color> param3, Material param4) {
      this.metaConsumer = var3;
      this.material = var4;
   }

   public static BaseItemEnum get(String var0) {
      try {
         return valueOf(var0);
      } catch (IllegalArgumentException var2) {
         return LEATHER_HORSE_ARMOR;
      }
   }

   public static BaseItemEnum fromMaterial(Material var0) {
      BaseItemEnum var1;
      switch(var0) {
      case FILLED_MAP:
         var1 = FILLED_MAP;
         break;
      case LEATHER_BOOTS:
         var1 = LEATHER_BOOTS;
         break;
      case LEATHER_CHESTPLATE:
         var1 = LEATHER_CHESTPLATE;
         break;
      case LEATHER_HORSE_ARMOR:
         var1 = LEATHER_HORSE_ARMOR;
         break;
      case LEATHER_LEGGINGS:
         var1 = LEATHER_LEGGINGS;
         break;
      case LINGERING_POTION:
         var1 = LINGERING_POTION;
         break;
      case POTION:
         var1 = POTION;
         break;
      case SPLASH_POTION:
         var1 = SPLASH_POTION;
         break;
      case TIPPED_ARROW:
         var1 = TIPPED_ARROW;
         break;
      default:
         var1 = null;
      }

      return var1;
   }

   private static BaseItemEnum[] $values() {
      return new BaseItemEnum[]{FILLED_MAP, LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HORSE_ARMOR, LEATHER_LEGGINGS, LINGERING_POTION, POTION, SPLASH_POTION, TIPPED_ARROW};
   }

   public void color(ItemMeta var1, Color var2) {
      this.metaConsumer.accept(var1, var2);
   }

   public ItemStack create() {
      return new ItemStack(this.material);
   }

   public ItemStack create(Color var1, int var2) {
      ItemStack var3 = this.create();
      ItemMeta var4 = var3.getItemMeta();
      var4.setCustomModelData(var2);
      this.color(var4, var1);
      var3.setItemMeta(var4);
      return var3;
   }

   public Material getMaterial() {
      return this.material;
   }

   // $FF: synthetic method
   private static BaseItemEnum[] $values$() {
      return new BaseItemEnum[]{FILLED_MAP, LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HORSE_ARMOR, LEATHER_LEGGINGS, LINGERING_POTION, POTION, SPLASH_POTION, TIPPED_ARROW};
   }

   private static class Cons {
      private static final BiConsumer<ItemMeta, Color> MAP_CONSUMER = (var0, var1) -> {
         MapMeta var2 = (MapMeta)var0;
         var2.setColor(var1);
      };
      private static final BiConsumer<ItemMeta, Color> LEATHER_CONSUMER = (var0, var1) -> {
         LeatherArmorMeta var2 = (LeatherArmorMeta)var0;
         var2.setColor(var1);
      };
      private static final BiConsumer<ItemMeta, Color> POTION_CONSUMER = (var0, var1) -> {
         PotionMeta var2 = (PotionMeta)var0;
         var2.setColor(var1);
      };
   }
}
