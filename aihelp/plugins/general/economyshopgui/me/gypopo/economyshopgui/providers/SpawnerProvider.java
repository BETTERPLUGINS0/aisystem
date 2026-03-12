package me.gypopo.economyshopgui.providers;

import javax.annotation.Nullable;
import me.gypopo.economyshopgui.objects.ShopItem;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface SpawnerProvider {
   String getProviderName();

   ItemStack setSpawnedType(@Nullable ShopItem var1, ItemStack var2, EntityType var3);

   boolean isShopSpawner(ItemStack var1, ItemStack var2);

   Double getSpawnerSellPrice(ItemStack var1, double var2);

   String getSpawnedType(ItemStack var1);
}
