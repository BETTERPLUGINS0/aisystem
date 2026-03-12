package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.data.limbo.persistence.LimboPersistence;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.service.BukkitService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

class LimboPlayerViewer implements DebugSection {
   @Inject
   private LimboService limboService;
   @Inject
   private LimboPersistence limboPersistence;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private PermissionsManager permissionsManager;

   public String getName() {
      return "limbo";
   }

   public String getDescription() {
      return "View LimboPlayers and player's \"limbo stats\"";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      if (arguments.isEmpty()) {
         sender.sendMessage(ChatColor.BLUE + "AuthMe limbo viewer");
         sender.sendMessage("/authme debug limbo <player>: show a player's limbo info");
         sender.sendMessage("Available limbo records: " + DebugSectionUtils.applyToLimboPlayersMap(this.limboService, Map::keySet));
      } else {
         LimboPlayer memoryLimbo = this.limboService.getLimboPlayer((String)arguments.get(0));
         Player player = this.bukkitService.getPlayerExact((String)arguments.get(0));
         LimboPlayer diskLimbo = player != null ? this.limboPersistence.getLimboPlayer(player) : null;
         if (memoryLimbo == null && player == null) {
            sender.sendMessage(ChatColor.BLUE + "No AuthMe limbo data");
            sender.sendMessage("No limbo data and no player online with name '" + (String)arguments.get(0) + "'");
         } else {
            sender.sendMessage(ChatColor.BLUE + "Player / limbo / disk limbo info for '" + (String)arguments.get(0) + "'");
            (new LimboPlayerViewer.InfoDisplayer(sender, player, memoryLimbo, diskLimbo)).sendEntry("Is op", ServerOperator::isOp, LimboPlayer::isOperator).sendEntry("Walk speed", Player::getWalkSpeed, LimboPlayer::getWalkSpeed).sendEntry("Can fly", Player::getAllowFlight, LimboPlayer::isCanFly).sendEntry("Fly speed", Player::getFlySpeed, LimboPlayer::getFlySpeed).sendEntry("Location", (p) -> {
               return DebugSectionUtils.formatLocation(p.getLocation());
            }, (l) -> {
               return DebugSectionUtils.formatLocation(l.getLocation());
            }).sendEntry("Prim. group", (p) -> {
               return this.permissionsManager.hasGroupSupport() ? this.permissionsManager.getPrimaryGroup(p) : "N/A";
            }, LimboPlayer::getGroups);
         }
      }
   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.LIMBO_PLAYER_VIEWER;
   }

   private static final class InfoDisplayer {
      private final CommandSender sender;
      private final Optional<Player> player;
      private final Optional<LimboPlayer> memoryLimbo;
      private final Optional<LimboPlayer> diskLimbo;

      InfoDisplayer(CommandSender sender, Player player, LimboPlayer memoryLimbo, LimboPlayer diskLimbo) {
         this.sender = sender;
         this.player = Optional.ofNullable(player);
         this.memoryLimbo = Optional.ofNullable(memoryLimbo);
         this.diskLimbo = Optional.ofNullable(diskLimbo);
         if (memoryLimbo == null) {
            sender.sendMessage("Note: no Limbo information available");
         }

         if (player == null) {
            sender.sendMessage("Note: player is not online");
         } else if (diskLimbo == null) {
            sender.sendMessage("Note: no Limbo on disk available");
         }

      }

      <T> LimboPlayerViewer.InfoDisplayer sendEntry(String title, Function<Player, T> playerGetter, Function<LimboPlayer, T> limboGetter) {
         this.sender.sendMessage(title + ": " + getData(this.player, playerGetter) + " / " + getData(this.memoryLimbo, limboGetter) + " / " + getData(this.diskLimbo, limboGetter));
         return this;
      }

      static <E, T> String getData(Optional<E> entity, Function<E, T> getter) {
         return (String)entity.map(getter).map(String::valueOf).orElse(" -- ");
      }
   }
}
