package ac.grim.grimac.platform.bukkit.player;

import ac.grim.grimac.platform.api.player.PlatformInventory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Generated;
import org.bukkit.entity.Player;

public class BukkitPlatformInventory implements PlatformInventory {
   private final Player bukkitPlayer;

   public ItemStack getItemInHand() {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getItemInHand());
   }

   public ItemStack getItemInOffHand() {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getItemInOffHand());
   }

   public ItemStack getStack(int bukkitSlot, int vanillaSlot) {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getItem(bukkitSlot));
   }

   public ItemStack getHelmet() {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getHelmet());
   }

   public ItemStack getChestplate() {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getChestplate());
   }

   public ItemStack getLeggings() {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getLeggings());
   }

   public ItemStack getBoots() {
      return SpigotConversionUtil.fromBukkitItemStack(this.bukkitPlayer.getInventory().getBoots());
   }

   public ItemStack[] getContents() {
      org.bukkit.inventory.ItemStack[] bukkitItems = this.bukkitPlayer.getInventory().getContents();
      ItemStack[] items = new ItemStack[bukkitItems.length];

      for(int i = 0; i < bukkitItems.length; ++i) {
         if (bukkitItems[i] != null) {
            items[i] = SpigotConversionUtil.fromBukkitItemStack(bukkitItems[i]);
         }
      }

      return items;
   }

   public String getOpenInventoryKey() {
      return this.bukkitPlayer.getOpenInventory().getType().toString();
   }

   @Generated
   public BukkitPlatformInventory(Player bukkitPlayer) {
      this.bukkitPlayer = bukkitPlayer;
   }
}
