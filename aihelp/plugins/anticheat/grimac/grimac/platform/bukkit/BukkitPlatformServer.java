package ac.grim.grimac.platform.bukkit;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BukkitPlatformServer implements PlatformServer {
   public String getPlatformImplementationString() {
      return Bukkit.getVersion();
   }

   public void dispatchCommand(Sender sender, String command) {
      CommandSender commandSender = GrimACBukkitLoaderPlugin.LOADER.getBukkitSenderFactory().reverse(sender);
      Bukkit.dispatchCommand(commandSender, command);
   }

   public Sender getConsoleSender() {
      return GrimACBukkitLoaderPlugin.LOADER.getBukkitSenderFactory().map((CommandSender)Bukkit.getConsoleSender());
   }

   public void registerOutgoingPluginChannel(String name) {
      GrimACBukkitLoaderPlugin.LOADER.getServer().getMessenger().registerOutgoingPluginChannel(GrimACBukkitLoaderPlugin.LOADER, name);
   }

   public double getTPS() {
      return GrimAPI.INSTANCE.getPlatform() == Platform.FOLIA ? Double.NaN : SpigotReflectionUtil.getTPS();
   }
}
