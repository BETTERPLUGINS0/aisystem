package me.gypopo.economyshopgui.commands.base;

import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;

public abstract class PluginCommands extends PluginCommand {
   public PluginCommands(EconomyShopGUI plugin, String commandName, String description, String usageMessage, String permission, List<String> aliases, List<String> disabledWorlds) {
      super(plugin, commandName, description, usageMessage, permission, disabledWorlds);
      this.setAliases(aliases);
   }
}
