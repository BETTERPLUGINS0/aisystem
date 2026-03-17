package es.outlook.adriansrj.spigui.item;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
   private final ItemStack stack;

   public ItemBuilder(Material var1) {
      this.stack = new ItemStack(var1);
   }

   public ItemBuilder(ItemStack var1) {
      this.stack = var1;
   }

   public ItemBuilder type(Material var1) {
      this.stack.setType(var1);
      return this;
   }

   public Material getType() {
      return this.stack.getType();
   }

   public ItemBuilder name(String var1) {
      ItemMeta var2 = this.stack.getItemMeta();
      var2.setDisplayName(ChatColor.translateAlternateColorCodes('&', var1));
      this.stack.setItemMeta(var2);
      return this;
   }

   public String getName() {
      return this.stack.hasItemMeta() && this.stack.getItemMeta().hasDisplayName() ? this.stack.getItemMeta().getDisplayName() : null;
   }

   public ItemBuilder amount(int var1) {
      this.stack.setAmount(var1);
      return this;
   }

   public int getAmount() {
      return this.stack.getAmount();
   }

   public ItemBuilder lore(String... var1) {
      return this.lore(Arrays.asList(var1));
   }

   public ItemBuilder lore(List<String> var1) {
      var1.replaceAll((var0) -> {
         return ChatColor.translateAlternateColorCodes('&', var0);
      });
      ItemMeta var2 = this.stack.getItemMeta();
      var2.setLore(var1);
      this.stack.setItemMeta(var2);
      return this;
   }

   public List<String> getLore() {
      return this.stack.hasItemMeta() && this.stack.getItemMeta().hasLore() ? this.stack.getItemMeta().getLore() : null;
   }

   public ItemBuilder color(ItemDataColor var1) {
      return this.durability(var1.getValue());
   }

   public ItemBuilder data(short var1) {
      return this.durability(var1);
   }

   public ItemBuilder durability(short var1) {
      this.stack.setDurability(var1);
      return this;
   }

   public short getDurability() {
      return this.stack.getDurability();
   }

   public ItemDataColor getColor() {
      return ItemDataColor.getByValue(this.stack.getDurability());
   }

   public ItemBuilder enchant(Enchantment var1, int var2) {
      this.stack.addUnsafeEnchantment(var1, var2);
      return this;
   }

   public ItemBuilder unenchant(Enchantment var1) {
      this.stack.removeEnchantment(var1);
      return this;
   }

   public ItemBuilder flag(ItemFlag... var1) {
      ItemMeta var2 = this.stack.getItemMeta();
      var2.addItemFlags(var1);
      this.stack.setItemMeta(var2);
      return this;
   }

   public ItemBuilder deflag(ItemFlag... var1) {
      ItemMeta var2 = this.stack.getItemMeta();
      var2.removeItemFlags(var1);
      this.stack.setItemMeta(var2);
      return this;
   }

   public ItemBuilder skullOwner(String var1) {
      if (!(this.stack.getItemMeta() instanceof SkullMeta)) {
         return this;
      } else {
         this.stack.setDurability((short)3);
         SkullMeta var2 = (SkullMeta)this.stack.getItemMeta();
         var2.setOwner(var1);
         this.stack.setItemMeta(var2);
         return this;
      }
   }

   public ItemBuilder ifThen(Predicate<ItemBuilder> var1, Function<ItemBuilder, Object> var2) {
      if (var1.test(this)) {
         var2.apply(this);
      }

      return this;
   }

   public ItemStack build() {
      return this.get();
   }

   public ItemStack get() {
      return this.stack;
   }
}
