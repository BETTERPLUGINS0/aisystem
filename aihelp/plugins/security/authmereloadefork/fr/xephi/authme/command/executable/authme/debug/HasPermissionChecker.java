package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.AdminPermission;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.DefaultPermission;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerPermission;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.service.BukkitService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class HasPermissionChecker implements DebugSection {
   static final List<Class<? extends PermissionNode>> PERMISSION_NODE_CLASSES = ImmutableList.of(AdminPermission.class, PlayerPermission.class, PlayerStatePermission.class, DebugSectionPermissions.class);
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private BukkitService bukkitService;

   public String getName() {
      return "perm";
   }

   public String getDescription() {
      return "Checks if a player has a given permission";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      sender.sendMessage(ChatColor.BLUE + "AuthMe permission check");
      if (arguments.size() < 2) {
         sender.sendMessage("Check if a player has permission:");
         sender.sendMessage("Example: /authme debug perm bobby my.perm.node");
         sender.sendMessage("Permission system type used: " + this.permissionsManager.getPermissionSystem());
      } else {
         String playerName = (String)arguments.get(0);
         String permissionNode = (String)arguments.get(1);
         Player player = this.bukkitService.getPlayerExact(playerName);
         PermissionsManager var10002;
         if (player == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (offlinePlayer == null) {
               sender.sendMessage(ChatColor.DARK_RED + "Player '" + playerName + "' does not exist");
            } else {
               sender.sendMessage("Player '" + playerName + "' not online; checking with offline player");
               var10002 = this.permissionsManager;
               Objects.requireNonNull(var10002);
               performPermissionCheck(offlinePlayer, permissionNode, var10002::hasPermissionOffline, sender);
            }
         } else {
            var10002 = this.permissionsManager;
            Objects.requireNonNull(var10002);
            performPermissionCheck(player, permissionNode, var10002::hasPermission, sender);
         }

      }
   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.HAS_PERMISSION_CHECK;
   }

   private static <P extends OfflinePlayer> void performPermissionCheck(P player, String node, BiFunction<P, PermissionNode, Boolean> permissionChecker, CommandSender sender) {
      PermissionNode permNode = getPermissionNode(sender, node);
      if ((Boolean)permissionChecker.apply(player, permNode)) {
         sender.sendMessage(ChatColor.DARK_GREEN + "Success: player '" + player.getName() + "' has permission '" + node + "'");
      } else {
         sender.sendMessage(ChatColor.DARK_RED + "Check failed: player '" + player.getName() + "' does NOT have permission '" + node + "'");
      }

   }

   private static PermissionNode getPermissionNode(CommandSender sender, String node) {
      Optional<? extends PermissionNode> permNode = PERMISSION_NODE_CLASSES.stream().map(Class::getEnumConstants).flatMap(Arrays::stream).filter((perm) -> {
         return perm.getNode().equals(node);
      }).findFirst();
      if (permNode.isPresent()) {
         return (PermissionNode)permNode.get();
      } else {
         sender.sendMessage("Did not detect AuthMe permission; using default permission = DENIED");
         return createPermNode(node);
      }
   }

   private static PermissionNode createPermNode(final String node) {
      return new PermissionNode() {
         public String getNode() {
            return node;
         }

         public DefaultPermission getDefaultPermission() {
            return DefaultPermission.NOT_ALLOWED;
         }
      };
   }
}
