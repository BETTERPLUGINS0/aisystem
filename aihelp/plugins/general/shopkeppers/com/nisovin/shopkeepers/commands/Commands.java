package com.nisovin.shopkeepers.commands;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.commands.shopkeepers.ShopkeepersCommand;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Commands {
   private final SKShopkeepersPlugin plugin;
   private final Confirmations confirmations;
   @Nullable
   private ShopkeepersCommand shopkeepersCommand;

   public Commands(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
      this.confirmations = new Confirmations(plugin);
   }

   public void onEnable() {
      this.confirmations.onEnable();
      this.shopkeepersCommand = new ShopkeepersCommand(this.plugin, this.confirmations);
   }

   public void onDisable() {
      this.confirmations.onDisable();
   }

   public void onPlayerQuit(Player player) {
      assert player != null;

      this.confirmations.onPlayerQuit(player);
   }

   public ShopkeepersCommand getShopkeepersCommand() {
      return (ShopkeepersCommand)Validate.State.notNull(this.shopkeepersCommand, (String)"The commands have not yet been set up!");
   }
}
