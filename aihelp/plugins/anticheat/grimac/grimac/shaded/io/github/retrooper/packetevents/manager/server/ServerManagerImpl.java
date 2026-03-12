package ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ServerManagerImpl implements ServerManager {
   private ServerVersion serverVersion;

   private ServerVersion resolveVersionNoCache() {
      Plugin plugin = (Plugin)PacketEvents.getAPI().getPlugin();
      String bukkitVersion = Bukkit.getBukkitVersion();
      ServerVersion fallbackVersion = ServerVersion.V_1_8_8;
      String failureToDetectVersionMsg = "Your server software is preventing us from checking the Minecraft Server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the Server version is " + fallbackVersion.name() + "...\n If you need assistance, join our Discord server: https://discord.gg/DVHxPPxHZc";
      if (bukkitVersion.contains("Unknown")) {
         plugin.getLogger().warning(failureToDetectVersionMsg);
         return fallbackVersion;
      } else {
         ServerVersion[] var5 = ServerVersion.reversedValues();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ServerVersion val = var5[var7];
            if (bukkitVersion.contains(val.getReleaseName())) {
               return val;
            }
         }

         plugin.getLogger().warning(failureToDetectVersionMsg);
         return fallbackVersion;
      }
   }

   public ServerVersion getVersion() {
      if (this.serverVersion == null) {
         this.serverVersion = this.resolveVersionNoCache();
      }

      return this.serverVersion;
   }

   public Object getRegistryCacheKey(User user, ClientVersion version) {
      return GlobalRegistryHolder.getGlobalRegistryCacheKey(user, version);
   }
}
