package me.PM2.infinitevehicles.commands;

import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

class ProxyCommandMap extends SimpleCommandMap {
   private BukkitCommandManager manager;
   CommandMap proxied;

   ProxyCommandMap(BukkitCommandManager manager, CommandMap proxied) {
      super(Bukkit.getServer());
      this.manager = var1;
      this.proxied = var2;
   }

   public void registerAll(String fallbackPrefix, List<Command> commands) {
      this.proxied.registerAll(var1, var2);
   }

   public boolean register(String label, String fallbackPrefix, Command command) {
      return this.isOurCommand(var3) ? super.register(var1, var2, var3) : this.proxied.register(var1, var2, var3);
   }

   boolean isOurCommand(String cmdLine) {
      String[] var2 = ACFPatterns.SPACE.split(var1);
      return var2.length != 0 && this.isOurCommand((Command)this.knownCommands.get(var2[0].toLowerCase(Locale.ENGLISH)));
   }

   boolean isOurCommand(Command command) {
      return var1 instanceof RootCommand && ((RootCommand)var1).getManager() == this.manager;
   }

   public boolean register(String fallbackPrefix, Command command) {
      return this.isOurCommand(var2) ? super.register(var1, var2) : this.proxied.register(var1, var2);
   }

   public boolean dispatch(CommandSender sender, String cmdLine) {
      return this.isOurCommand(var2) ? super.dispatch(var1, var2) : this.proxied.dispatch(var1, var2);
   }

   public void clearCommands() {
      super.clearCommands();
      this.proxied.clearCommands();
   }

   public Command getCommand(String name) {
      return this.isOurCommand(var1) ? super.getCommand(var1) : this.proxied.getCommand(var1);
   }

   public List<String> tabComplete(CommandSender sender, String cmdLine) {
      return this.isOurCommand(var2) ? super.tabComplete(var1, var2) : this.proxied.tabComplete(var1, var2);
   }
}
