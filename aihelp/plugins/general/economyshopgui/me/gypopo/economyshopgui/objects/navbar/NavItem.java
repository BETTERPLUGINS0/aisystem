package me.gypopo.economyshopgui.objects.navbar;

import me.gypopo.economyshopgui.util.EcoType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NavItem {
   boolean isDisabled();

   String getAction();

   String getAction(int var1, int var2);

   ItemStack getItem(Player var1, EcoType var2);

   ItemStack getItem(Player var1, EcoType var2, int var3, int var4);
}
