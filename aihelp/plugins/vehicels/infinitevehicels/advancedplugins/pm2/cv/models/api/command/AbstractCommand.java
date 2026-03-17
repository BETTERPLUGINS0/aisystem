package advancedplugins.pm2.cv.models.api.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements TabExecutor {
   protected final JavaPlugin plugin;
   private final Map<String, AbstractCommand> subCommands;
   private final Map<String, AbstractCommand> subCommandAliases;

   public AbstractCommand(AbstractCommand var1) {
      this(var1.getPlugin());
   }

   public AbstractCommand(JavaPlugin var1) {
      this.subCommands = new ConcurrentHashMap();
      this.subCommandAliases = new ConcurrentHashMap();
      this.plugin = var1;
   }

   public final void addSubCommands(AbstractCommand... var1) {
      AbstractCommand[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AbstractCommand var5 = var2[var4];
         this.subCommands.put(var5.getName(), var5);
         String[] var6 = var5.getAliases();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            this.subCommandAliases.put(var9, var5);
         }
      }

   }

   public boolean onCommand(@NotNull CommandSender var1, @NotNull Command var2, @NotNull String var3, String[] var4) {
      if (this.getPermissionNode() != null && !var1.hasPermission(this.getPermissionNode()) && !var1.hasPermission("infinitemodel.admin")) {
         var1.sendMessage(Component.text("You don't have permission to do this!").color(NamedTextColor.RED));
         return true;
      } else if (!this.isConsoleFriendly() && !(var1 instanceof Player)) {
         var1.sendMessage(Component.text("Only players can do this!").color(NamedTextColor.RED));
         return true;
      } else {
         AbstractCommand var5;
         if (var4.length > 0 && this.subCommands.get(var4[0].toLowerCase()) != null) {
            var5 = (AbstractCommand)this.subCommands.get(var4[0].toLowerCase());
            return var5.onCommand(var1, var2, var3, (String[])Arrays.copyOfRange(var4, 1, var4.length));
         } else if (var4.length > 0 && this.subCommandAliases.get(var4[0].toLowerCase()) != null) {
            var5 = (AbstractCommand)this.subCommandAliases.get(var4[0].toLowerCase());
            return var5.onCommand(var1, var2, var3, (String[])Arrays.copyOfRange(var4, 1, var4.length));
         } else {
            return this.onCommand(var1, var4);
         }
      }
   }

   public List<String> onTabComplete(@NotNull CommandSender var1, @NotNull Command var2, @NotNull String var3, String[] var4) {
      if (this.getPermissionNode() != null && !var1.hasPermission(this.getPermissionNode())) {
         return null;
      } else {
         AbstractCommand var5;
         if (var4.length > 1 && this.subCommands.get(var4[0].toLowerCase()) != null) {
            var5 = (AbstractCommand)this.subCommands.get(var4[0].toLowerCase());
            return var5.onTabComplete(var1, var2, var3, (String[])Arrays.copyOfRange(var4, 1, var4.length));
         } else if (var4.length > 1 && this.subCommandAliases.get(var4[0].toLowerCase()) != null) {
            var5 = (AbstractCommand)this.subCommandAliases.get(var4[0].toLowerCase());
            return var5.onTabComplete(var1, var2, var3, (String[])Arrays.copyOfRange(var4, 1, var4.length));
         } else {
            Object var6 = this.onTabComplete(var1, var4);
            if (var6 == null && var4.length == 1) {
               var6 = new ArrayList();
               StringUtil.copyPartialMatches(var4[0], this.subCommands.keySet(), (Collection)var6);
            }

            return (List)var6;
         }
      }
   }

   public abstract boolean onCommand(CommandSender var1, String[] var2);

   public abstract List<String> onTabComplete(CommandSender var1, String[] var2);

   public abstract String getPermissionNode();

   public abstract boolean isConsoleFriendly();

   public String[] getAliases() {
      return new String[0];
   }

   public abstract String getName();

   protected JavaPlugin getPlugin() {
      return this.plugin;
   }
}
