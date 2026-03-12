package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;

public interface PlatformInventory {
   ItemStack getItemInHand();

   ItemStack getItemInOffHand();

   ItemStack getStack(int var1, int var2);

   ItemStack getHelmet();

   ItemStack getChestplate();

   ItemStack getLeggings();

   ItemStack getBoots();

   ItemStack[] getContents();

   String getOpenInventoryKey();
}
