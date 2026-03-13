package me.gypopo.economyshopgui.events;

import java.util.ArrayList;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.SkullUtil;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
   private final EconomyShopGUI plugin;
   private final boolean joinMessage;
   private ArrayList<Integer> versions = new ArrayList();

   public JoinEvent(EconomyShopGUI plugin, boolean joinMessage) {
      this.plugin = plugin;
      this.joinMessage = joinMessage;
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent e) {
      Player player = e.getPlayer();
      if (this.plugin.updateChecker != null && PermissionsCache.hasPermission(player, "EconomyShopGUI.notifyUpdate")) {
         this.checkForUpdates(player);
      }

      if (this.joinMessage) {
         this.sendJoinMessage(player);
      }

      SkullUtil.updateTexture(e.getPlayer());
      this.plugin.runTaskLater(() -> {
         UserManager.loadUser(e.getPlayer());
      }, 5L);
   }

   private void checkForUpdates(Player p) {
      if (this.plugin.updateChecker.updateAvailable) {
         this.plugin.runTaskLater(() -> {
            SendMessage.chatToPlayer(p, Lang.UPDATE_AVAILABLE.get().replace("%plugin_version%", this.plugin.updateChecker.CURRENT_VERSION.getVer()).replace("%latest_version%", this.plugin.updateChecker.LATEST_VERSION.getVer()));
            SendMessage.chatToPlayer(p, ChatColor.GREEN + (this.plugin.updateChecker.LATEST_VERSION.isDev() ? "Download at: https://open-beta.gpplugins.com/economyshopgui/" : "Download at: https://www.spigotmc.org/resources/economyshopgui.69927/"));
         }, 5L);
      }

   }

   private void sendJoinMessage(Player p) {
      OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(p.getUniqueId());
      if (offlinePlayer.hasPlayedBefore()) {
         SendMessage.chatToPlayer(p, Lang.JOIN_MESSAGE.get().replace("%balance%", this.plugin.formatPrice((EcoType)null, this.plugin.getEcoHandler().getDefaultProvider().getBalance(p))));
      }

   }
}
