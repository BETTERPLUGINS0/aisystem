package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BaseCommand extends Command implements CommandExecutor, TabCompleter {
   private static PluginCommand getPluginCommand(JavaPlugin plugin, String commandName) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notEmpty(commandName, "commandName is null or empty");
      PluginCommand bukkitCommand = plugin.getCommand(commandName);
      return (PluginCommand)Validate.State.notNull(bukkitCommand, (Supplier)(() -> {
         return "Could not find command: " + commandName;
      }));
   }

   private static List<String> getAliases(PluginCommand bukkitCommand) {
      List<String> aliases = bukkitCommand.getAliases();
      Validate.noNullElements(aliases, (String)"bukkitCommand contains null aliases");
      return new ArrayList((Collection)Unsafe.cast(aliases));
   }

   public BaseCommand(JavaPlugin plugin, String commandName) {
      this(getPluginCommand(plugin, commandName));
   }

   public BaseCommand(PluginCommand bukkitCommand) {
      super(((PluginCommand)Validate.notNull(bukkitCommand, (String)"bukkitCommand is null")).getName(), getAliases(bukkitCommand));
      String desc = bukkitCommand.getDescription();
      if (!desc.isEmpty()) {
         this.setDescription(Text.of(desc));
      }

      String permission = bukkitCommand.getPermission();
      if (permission != null && !permission.isEmpty()) {
         this.setPermission(permission);
      }

      bukkitCommand.setExecutor((CommandExecutor)Unsafe.initialized(this));
      bukkitCommand.setTabCompleter((TabCompleter)Unsafe.initialized(this));
   }

   public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String commandAlias, String[] args) {
      CommandInput input = new CommandInput(sender, this, commandAlias, args);
      this.handleCommand(input);
      return true;
   }

   @Nullable
   public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String commandAlias, String[] args) {
      CommandInput input = new CommandInput(sender, this, commandAlias, args);
      return (List)Unsafe.cast(this.handleTabCompletion(input));
   }
}
