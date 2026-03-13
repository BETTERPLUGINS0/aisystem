package github.nighter.smartspawner.spawner.gui.storage.utils;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemClickHandler {
   void handle(Player var1, Inventory var2, int var3, ItemStack var4, SpawnerData var5);
}
