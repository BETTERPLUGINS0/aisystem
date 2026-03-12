package net.milkbowl.vault.permission;

import java.util.Iterator;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public abstract class Permission {
   protected static final Logger log = Logger.getLogger("Minecraft");
   protected Plugin plugin = null;

   public abstract String getName();

   public abstract boolean isEnabled();

   public abstract boolean hasSuperPermsCompat();

   /** @deprecated */
   @Deprecated
   public boolean has(String world, String player, String permission) {
      return world == null ? this.playerHas((String)null, player, permission) : this.playerHas(world, player, permission);
   }

   /** @deprecated */
   @Deprecated
   public boolean has(World world, String player, String permission) {
      return world == null ? this.playerHas((String)null, player, permission) : this.playerHas(world.getName(), player, permission);
   }

   public boolean has(CommandSender sender, String permission) {
      return sender.hasPermission(permission);
   }

   public boolean has(Player player, String permission) {
      return player.hasPermission(permission);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean playerHas(String var1, String var2, String var3);

   /** @deprecated */
   @Deprecated
   public boolean playerHas(World world, String player, String permission) {
      return world == null ? this.playerHas((String)null, player, permission) : this.playerHas(world.getName(), player, permission);
   }

   public boolean playerHas(String world, OfflinePlayer player, String permission) {
      return world == null ? this.has((String)null, player.getName(), permission) : this.has(world, player.getName(), permission);
   }

   public boolean playerHas(Player player, String permission) {
      return this.has(player, permission);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean playerAdd(String var1, String var2, String var3);

   /** @deprecated */
   @Deprecated
   public boolean playerAdd(World world, String player, String permission) {
      return world == null ? this.playerAdd((String)null, player, permission) : this.playerAdd(world.getName(), player, permission);
   }

   public boolean playerAdd(String world, OfflinePlayer player, String permission) {
      return world == null ? this.playerAdd((String)null, player.getName(), permission) : this.playerAdd(world, player.getName(), permission);
   }

   public boolean playerAdd(Player player, String permission) {
      return this.playerAdd((String)player.getWorld().getName(), (OfflinePlayer)player, permission);
   }

   public boolean playerAddTransient(OfflinePlayer player, String permission) throws UnsupportedOperationException {
      if (player.isOnline()) {
         return this.playerAddTransient((Player)player, permission);
      } else {
         throw new UnsupportedOperationException(this.getName() + " does not support offline player transient permissions!");
      }
   }

   public boolean playerAddTransient(Player player, String permission) {
      Iterator var3 = player.getEffectivePermissions().iterator();

      PermissionAttachmentInfo paInfo;
      do {
         if (!var3.hasNext()) {
            PermissionAttachment attach = player.addAttachment(this.plugin);
            attach.setPermission(permission, true);
            return true;
         }

         paInfo = (PermissionAttachmentInfo)var3.next();
      } while(paInfo.getAttachment() == null || !paInfo.getAttachment().getPlugin().equals(this.plugin));

      paInfo.getAttachment().setPermission(permission, true);
      return true;
   }

   public boolean playerAddTransient(String worldName, OfflinePlayer player, String permission) {
      return this.playerAddTransient(player, permission);
   }

   public boolean playerAddTransient(String worldName, Player player, String permission) {
      return this.playerAddTransient(player, permission);
   }

   public boolean playerRemoveTransient(String worldName, OfflinePlayer player, String permission) {
      return this.playerRemoveTransient(player, permission);
   }

   public boolean playerRemoveTransient(String worldName, Player player, String permission) {
      return this.playerRemoveTransient((OfflinePlayer)player, permission);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean playerRemove(String var1, String var2, String var3);

   public boolean playerRemove(String world, OfflinePlayer player, String permission) {
      return world == null ? this.playerRemove((String)null, player.getName(), permission) : this.playerRemove(world, player.getName(), permission);
   }

   /** @deprecated */
   @Deprecated
   public boolean playerRemove(World world, String player, String permission) {
      return world == null ? this.playerRemove((String)null, player, permission) : this.playerRemove(world.getName(), player, permission);
   }

   public boolean playerRemove(Player player, String permission) {
      return this.playerRemove((String)player.getWorld().getName(), (OfflinePlayer)player, permission);
   }

   public boolean playerRemoveTransient(OfflinePlayer player, String permission) {
      return player.isOnline() ? this.playerRemoveTransient((Player)player, permission) : false;
   }

   public boolean playerRemoveTransient(Player player, String permission) {
      Iterator var3 = player.getEffectivePermissions().iterator();

      PermissionAttachmentInfo paInfo;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         paInfo = (PermissionAttachmentInfo)var3.next();
      } while(paInfo.getAttachment() == null || !paInfo.getAttachment().getPlugin().equals(this.plugin));

      paInfo.getAttachment().unsetPermission(permission);
      return true;
   }

   public abstract boolean groupHas(String var1, String var2, String var3);

   public boolean groupHas(World world, String group, String permission) {
      return world == null ? this.groupHas((String)null, group, permission) : this.groupHas(world.getName(), group, permission);
   }

   public abstract boolean groupAdd(String var1, String var2, String var3);

   public boolean groupAdd(World world, String group, String permission) {
      return world == null ? this.groupAdd((String)null, group, permission) : this.groupAdd(world.getName(), group, permission);
   }

   public abstract boolean groupRemove(String var1, String var2, String var3);

   public boolean groupRemove(World world, String group, String permission) {
      return world == null ? this.groupRemove((String)null, group, permission) : this.groupRemove(world.getName(), group, permission);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean playerInGroup(String var1, String var2, String var3);

   /** @deprecated */
   @Deprecated
   public boolean playerInGroup(World world, String player, String group) {
      return world == null ? this.playerInGroup((String)null, player, group) : this.playerInGroup(world.getName(), player, group);
   }

   public boolean playerInGroup(String world, OfflinePlayer player, String group) {
      return world == null ? this.playerInGroup((String)null, player.getName(), group) : this.playerInGroup(world, player.getName(), group);
   }

   public boolean playerInGroup(Player player, String group) {
      return this.playerInGroup((String)player.getWorld().getName(), (OfflinePlayer)player, group);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean playerAddGroup(String var1, String var2, String var3);

   /** @deprecated */
   @Deprecated
   public boolean playerAddGroup(World world, String player, String group) {
      return world == null ? this.playerAddGroup((String)null, player, group) : this.playerAddGroup(world.getName(), player, group);
   }

   public boolean playerAddGroup(String world, OfflinePlayer player, String group) {
      return world == null ? this.playerAddGroup((String)null, player.getName(), group) : this.playerAddGroup(world, player.getName(), group);
   }

   public boolean playerAddGroup(Player player, String group) {
      return this.playerAddGroup((String)player.getWorld().getName(), (OfflinePlayer)player, group);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean playerRemoveGroup(String var1, String var2, String var3);

   /** @deprecated */
   @Deprecated
   public boolean playerRemoveGroup(World world, String player, String group) {
      return world == null ? this.playerRemoveGroup((String)null, player, group) : this.playerRemoveGroup(world.getName(), player, group);
   }

   public boolean playerRemoveGroup(String world, OfflinePlayer player, String group) {
      return world == null ? this.playerRemoveGroup((String)null, player.getName(), group) : this.playerRemoveGroup(world, player.getName(), group);
   }

   public boolean playerRemoveGroup(Player player, String group) {
      return this.playerRemoveGroup((String)player.getWorld().getName(), (OfflinePlayer)player, group);
   }

   /** @deprecated */
   @Deprecated
   public abstract String[] getPlayerGroups(String var1, String var2);

   /** @deprecated */
   @Deprecated
   public String[] getPlayerGroups(World world, String player) {
      return world == null ? this.getPlayerGroups((String)null, player) : this.getPlayerGroups(world.getName(), player);
   }

   public String[] getPlayerGroups(String world, OfflinePlayer player) {
      return this.getPlayerGroups(world, player.getName());
   }

   public String[] getPlayerGroups(Player player) {
      return this.getPlayerGroups((String)player.getWorld().getName(), (OfflinePlayer)player);
   }

   /** @deprecated */
   @Deprecated
   public abstract String getPrimaryGroup(String var1, String var2);

   /** @deprecated */
   @Deprecated
   public String getPrimaryGroup(World world, String player) {
      return world == null ? this.getPrimaryGroup((String)null, player) : this.getPrimaryGroup(world.getName(), player);
   }

   public String getPrimaryGroup(String world, OfflinePlayer player) {
      return this.getPrimaryGroup(world, player.getName());
   }

   public String getPrimaryGroup(Player player) {
      return this.getPrimaryGroup((String)player.getWorld().getName(), (OfflinePlayer)player);
   }

   public abstract String[] getGroups();

   public abstract boolean hasGroupSupport();
}
