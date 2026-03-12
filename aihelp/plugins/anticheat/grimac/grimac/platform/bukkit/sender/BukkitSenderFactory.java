package ac.grim.grimac.platform.bukkit.sender;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.platform.bukkit.BukkitAudiences;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BukkitSenderFactory extends SenderFactory<CommandSender> implements SenderMapper<CommandSender, Sender> {
   private final BukkitAudiences audiences;

   public BukkitSenderFactory() {
      this.audiences = BukkitAudiences.create(GrimACBukkitLoaderPlugin.LOADER);
   }

   protected String getName(CommandSender sender) {
      return sender instanceof Player ? sender.getName() : "Console";
   }

   protected UUID getUniqueId(CommandSender sender) {
      UUID var10000;
      if (sender instanceof Player) {
         Player player = (Player)sender;
         var10000 = player.getUniqueId();
      } else {
         var10000 = Sender.CONSOLE_UUID;
      }

      return var10000;
   }

   protected void sendMessage(CommandSender sender, String message) {
      sender.sendMessage(message);
   }

   protected void sendMessage(CommandSender sender, Component message) {
      if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender) && !(sender instanceof RemoteConsoleCommandSender)) {
         GrimAPI.INSTANCE.getScheduler().getGlobalRegionScheduler().run(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
            this.audiences.sender(sender).sendMessage(message);
         });
      } else {
         this.audiences.sender(sender).sendMessage(message);
      }

   }

   protected boolean hasPermission(CommandSender sender, String node) {
      return sender.hasPermission(node);
   }

   protected boolean hasPermission(CommandSender sender, String node, boolean defaultIfUnset) {
      return sender.hasPermission(new Permission(node, defaultIfUnset ? PermissionDefault.TRUE : PermissionDefault.FALSE));
   }

   protected void performCommand(CommandSender sender, String command) {
      throw new UnsupportedOperationException();
   }

   protected boolean isConsole(CommandSender sender) {
      return sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender;
   }

   protected boolean isPlayer(CommandSender sender) {
      return sender instanceof Player;
   }

   @NotNull
   public Sender map(@NotNull CommandSender base) {
      return this.wrap(base);
   }

   @NotNull
   public CommandSender reverse(@NotNull Sender mapped) {
      return (CommandSender)this.unwrap(mapped);
   }
}
