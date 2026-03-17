package me.PM2.infinitevehicles.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

public class BukkitRootCommand extends Command implements RootCommand, PluginIdentifiableCommand {
   private final BukkitCommandManager manager;
   private final String name;
   private BaseCommand defCommand;
   private SetMultimap<String, RegisteredCommand> subCommands = HashMultimap.create();
   private List<BaseCommand> children = new ArrayList();
   boolean isRegistered = false;

   protected BukkitRootCommand(BukkitCommandManager manager, String name) {
      super(var2);
      this.manager = var1;
      this.name = var2;
   }

   public String getDescription() {
      RegisteredCommand var1 = this.getDefaultRegisteredCommand();
      String var2 = null;
      if (var1 != null && !var1.getHelpText().isEmpty()) {
         var2 = var1.getHelpText();
      } else if (var1 != null && var1.scope.description != null) {
         var2 = var1.scope.description;
      } else if (this.defCommand.description != null) {
         var2 = this.defCommand.description;
      }

      return var2 != null ? this.manager.getLocales().replaceI18NStrings(var2) : super.getDescription();
   }

   public String getCommandName() {
      return this.name;
   }

   public List<String> tabComplete(CommandSender sender, String commandLabel, String[] args) {
      if (var2.contains(":")) {
         var2 = ACFPatterns.COLON.split(var2, 2)[1];
      }

      return this.getTabCompletions(this.manager.getCommandIssuer(var1), var2, var3);
   }

   public boolean execute(CommandSender sender, String commandLabel, String[] args) {
      if (var2.contains(":")) {
         var2 = ACFPatterns.COLON.split(var2, 2)[1];
      }

      this.execute(this.manager.getCommandIssuer(var1), var2, var3);
      return true;
   }

   public boolean testPermissionSilent(CommandSender target) {
      return this.hasAnyPermission(this.manager.getCommandIssuer(var1));
   }

   public void addChild(BaseCommand command) {
      if (this.defCommand == null || !var1.subCommands.get("__default").isEmpty()) {
         this.defCommand = var1;
      }

      this.addChildShared(this.children, this.subCommands, var1);
      this.setPermission(this.getUniquePermission());
   }

   public CommandManager getManager() {
      return this.manager;
   }

   public SetMultimap<String, RegisteredCommand> getSubCommands() {
      return this.subCommands;
   }

   public List<BaseCommand> getChildren() {
      return this.children;
   }

   public BaseCommand getDefCommand() {
      return this.defCommand;
   }

   public Plugin getPlugin() {
      return this.manager.getPlugin();
   }
}
