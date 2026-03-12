package fr.xephi.authme.initialization;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.PlayerUtils;
import java.util.Iterator;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OnShutdownPlayerSaver {
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Settings settings;
   @Inject
   private ValidationService validationService;
   @Inject
   private DataSource dataSource;
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private LimboService limboService;

   OnShutdownPlayerSaver() {
   }

   public void saveAllPlayers() {
      Iterator var1 = this.bukkitService.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.savePlayer(player);
      }

   }

   private void savePlayer(Player player) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      if (!PlayerUtils.isNpc(player) && !this.validationService.isUnrestricted(name)) {
         if (this.limboService.hasLimboPlayer(name)) {
            this.limboService.restoreData(player);
         } else {
            this.saveLoggedinPlayer(player);
         }

         this.playerCache.removePlayer(name);
      }
   }

   private void saveLoggedinPlayer(Player player) {
      if ((Boolean)this.settings.getProperty(RestrictionSettings.SAVE_QUIT_LOCATION)) {
         Location loc = this.spawnLoader.getPlayerLocationOrSpawn(player);
         PlayerAuth auth = PlayerAuth.builder().name(player.getName().toLowerCase(Locale.ROOT)).realName(player.getName()).location(loc).build();
         this.dataSource.updateQuitLoc(auth);
      }

   }
}
