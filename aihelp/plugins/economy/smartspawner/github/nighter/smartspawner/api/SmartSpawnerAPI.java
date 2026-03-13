package github.nighter.smartspawner.api;

import github.nighter.smartspawner.api.data.SpawnerDataDTO;
import github.nighter.smartspawner.api.data.SpawnerDataModifier;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface SmartSpawnerAPI {
   ItemStack createSpawnerItem(EntityType var1);

   ItemStack createSpawnerItem(EntityType var1, int var2);

   ItemStack createVanillaSpawnerItem(EntityType var1);

   ItemStack createVanillaSpawnerItem(EntityType var1, int var2);

   ItemStack createItemSpawnerItem(Material var1);

   ItemStack createItemSpawnerItem(Material var1, int var2);

   boolean isSmartSpawner(ItemStack var1);

   boolean isVanillaSpawner(ItemStack var1);

   boolean isItemSpawner(ItemStack var1);

   EntityType getSpawnerEntityType(ItemStack var1);

   Material getItemSpawnerMaterial(ItemStack var1);

   SpawnerDataDTO getSpawnerByLocation(Location var1);

   SpawnerDataDTO getSpawnerById(String var1);

   List<SpawnerDataDTO> getAllSpawners();

   SpawnerDataModifier getSpawnerModifier(String var1);
}
