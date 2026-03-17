package advancedplugins.pm2.cv.api.registry.types;

import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.registry.ConfigurationRegistryBase;
import advancedplugins.pm2.cv.api.util.Constants;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public final class ItemConfigurationRegistry extends ConfigurationRegistryBase<ItemConfiguration> {
   private static final Set<ItemConfiguration> DEFAULTS = new HashSet();

   @NotNull
   protected File getFolder() {
      return Constants.Files.ITEMS_FOLDER;
   }

   protected ItemConfiguration loadEntry(File var1) {
      return ItemConfiguration.load(YamlConfiguration.loadConfiguration(var1));
   }

   public void reload() {
      super.reload();
   }

   protected Set<ItemConfiguration> getDefaults() {
      return DEFAULTS;
   }

   protected void writeEntry(@NotNull ItemConfiguration var1, YamlConfiguration var2) {
      var1.write(var2);
   }

   static {
      DEFAULTS.add(new ItemConfiguration("fuel", Material.PLAYER_HEAD, "&4&lFuel", Arrays.asList("", "&7Fuel content: &l%f", ""), (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI4OWRlNDhhOWI4N2NmZDA3YzcwNGYyYmU1ZTVhOGNjNDVlODA3OWQzOGZhYWVkZjEzYjE1ZDE1YTEwYTcwYyJ9fX0=", new ItemConfiguration.FuelAction(40.0F)));
   }
}
