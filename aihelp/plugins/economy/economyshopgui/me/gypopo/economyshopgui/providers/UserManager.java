package me.gypopo.economyshopgui.providers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

public class UserManager {
   private final EconomyShopGUI plugin;
   private static final ArrayList<Integer> versions = new ArrayList();
   private static ViaAPI vv;
   private static FloodgateApi fAPI;
   private static String prefix = ".";
   private static boolean ps = false;
   private static final LoadingCache<UUID, User> USER_MAP = CacheBuilder.newBuilder().build(new CacheLoader<UUID, User>() {
      public User load(UUID uuid) throws Exception {
         return UserManager.loadUser(Bukkit.getPlayer(uuid));
      }
   });

   public UserManager(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public void init() {
      if (this.plugin.getServer().getPluginManager().getPlugin("ViaVersion") != null) {
         vv = Via.getAPI();
         this.plugin.runTaskLater(() -> {
            versions.addAll(vv.getServerVersion().supportedVersions());
         }, 5L);
      }

      if (this.plugin.getServer().getPluginManager().getPlugin("ProtocolSupport") != null) {
         versions.addAll((Collection)Arrays.stream(ProtocolVersion.getAllSupported()).map(ProtocolVersion::getId).collect(Collectors.toList()));
         ps = true;
      }

      if (this.plugin.getServer().getPluginManager().isPluginEnabled("floodgate")) {
         fAPI = FloodgateApi.getInstance();
         prefix = this.getPrefix();
      }

   }

   private String getPrefix() {
      File conf = new File(EconomyShopGUI.getInstance().getDataFolder().getParent() + "/floodgate/config.yml");
      if (!conf.exists()) {
         SendMessage.logDebugMessage("Failed to find player prefix for floodgate players, using default of '.'...");
         return ".";
      } else {
         FileConfiguration config = EconomyShopGUI.getInstance().loadConfiguration(conf, "floodgate config.yml");
         return config == null ? "." : config.getString("username-prefix", ".");
      }
   }

   public static User getUser(Player p) {
      try {
         return (User)USER_MAP.get(p.getUniqueId());
      } catch (ExecutionException var2) {
         return loadUser(p);
      }
   }

   public static User loadUser(Player p) {
      User user = new User(p);
      if (isPrPlayer(p)) {
         user.setPr(true);
      }

      if (isFloodgatePlayer(p)) {
         user.setBedrock(true);
      }

      return user;
   }

   private static boolean isPrPlayer(Player p) {
      if (vv != null) {
         return !versions.contains(vv.getPlayerVersion(p.getUniqueId()));
      } else {
         return ps ? versions.contains(ProtocolSupportAPI.getConnection(p).getVersion().getId()) : false;
      }
   }

   private static boolean isFloodgatePlayer(Player p) {
      return fAPI != null ? fAPI.isFloodgatePlayer(p.getUniqueId()) : p.getName().startsWith(prefix);
   }

   public void reloadPlayerData() {
      this.plugin.getServer().getOnlinePlayers().forEach((p) -> {
         USER_MAP.refresh(p.getUniqueId());
      });
   }
}
