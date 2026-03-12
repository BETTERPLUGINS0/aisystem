package net.milkbowl.vault.chat;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class Chat {
   private Permission perms;

   public Chat(Permission perms) {
      this.perms = perms;
   }

   public abstract String getName();

   public abstract boolean isEnabled();

   /** @deprecated */
   @Deprecated
   public abstract String getPlayerPrefix(String var1, String var2);

   public String getPlayerPrefix(String world, OfflinePlayer player) {
      return this.getPlayerPrefix(world, player.getName());
   }

   /** @deprecated */
   @Deprecated
   public String getPlayerPrefix(World world, String player) {
      return this.getPlayerPrefix(world.getName(), player);
   }

   public String getPlayerPrefix(Player player) {
      return this.getPlayerPrefix((String)player.getWorld().getName(), (OfflinePlayer)player);
   }

   /** @deprecated */
   @Deprecated
   public abstract void setPlayerPrefix(String var1, String var2, String var3);

   public void setPlayerPrefix(String world, OfflinePlayer player, String prefix) {
      this.setPlayerPrefix(world, player.getName(), prefix);
   }

   /** @deprecated */
   @Deprecated
   public void setPlayerPrefix(World world, String player, String prefix) {
      this.setPlayerPrefix(world.getName(), player, prefix);
   }

   public void setPlayerPrefix(Player player, String prefix) {
      this.setPlayerPrefix((String)player.getWorld().getName(), (OfflinePlayer)player, prefix);
   }

   /** @deprecated */
   @Deprecated
   public abstract String getPlayerSuffix(String var1, String var2);

   public String getPlayerSuffix(String world, OfflinePlayer player) {
      return this.getPlayerSuffix(world, player.getName());
   }

   /** @deprecated */
   @Deprecated
   public String getPlayerSuffix(World world, String player) {
      return this.getPlayerSuffix(world.getName(), player);
   }

   public String getPlayerSuffix(Player player) {
      return this.getPlayerSuffix((String)player.getWorld().getName(), (OfflinePlayer)player);
   }

   /** @deprecated */
   @Deprecated
   public abstract void setPlayerSuffix(String var1, String var2, String var3);

   public void setPlayerSuffix(String world, OfflinePlayer player, String suffix) {
      this.setPlayerSuffix(world, player.getName(), suffix);
   }

   /** @deprecated */
   @Deprecated
   public void setPlayerSuffix(World world, String player, String suffix) {
      this.setPlayerSuffix(world.getName(), player, suffix);
   }

   public void setPlayerSuffix(Player player, String suffix) {
      this.setPlayerSuffix((String)player.getWorld().getName(), (OfflinePlayer)player, suffix);
   }

   public abstract String getGroupPrefix(String var1, String var2);

   public String getGroupPrefix(World world, String group) {
      return this.getGroupPrefix(world.getName(), group);
   }

   public abstract void setGroupPrefix(String var1, String var2, String var3);

   public void setGroupPrefix(World world, String group, String prefix) {
      this.setGroupPrefix(world.getName(), group, prefix);
   }

   public abstract String getGroupSuffix(String var1, String var2);

   public String getGroupSuffix(World world, String group) {
      return this.getGroupSuffix(world.getName(), group);
   }

   public abstract void setGroupSuffix(String var1, String var2, String var3);

   public void setGroupSuffix(World world, String group, String suffix) {
      this.setGroupSuffix(world.getName(), group, suffix);
   }

   public int getPlayerInfoInteger(String world, OfflinePlayer player, String node, int defaultValue) {
      return this.getPlayerInfoInteger(world, player.getName(), node, defaultValue);
   }

   /** @deprecated */
   @Deprecated
   public abstract int getPlayerInfoInteger(String var1, String var2, String var3, int var4);

   /** @deprecated */
   @Deprecated
   public int getPlayerInfoInteger(World world, String player, String node, int defaultValue) {
      return this.getPlayerInfoInteger(world.getName(), player, node, defaultValue);
   }

   public int getPlayerInfoInteger(Player player, String node, int defaultValue) {
      return this.getPlayerInfoInteger((String)player.getWorld().getName(), (OfflinePlayer)player, node, defaultValue);
   }

   public void setPlayerInfoInteger(String world, OfflinePlayer player, String node, int value) {
      this.setPlayerInfoInteger(world, player.getName(), node, value);
   }

   /** @deprecated */
   @Deprecated
   public abstract void setPlayerInfoInteger(String var1, String var2, String var3, int var4);

   /** @deprecated */
   @Deprecated
   public void setPlayerInfoInteger(World world, String player, String node, int value) {
      this.setPlayerInfoInteger(world.getName(), player, node, value);
   }

   public void setPlayerInfoInteger(Player player, String node, int value) {
      this.setPlayerInfoInteger((String)player.getWorld().getName(), (OfflinePlayer)player, node, value);
   }

   public abstract int getGroupInfoInteger(String var1, String var2, String var3, int var4);

   public int getGroupInfoInteger(World world, String group, String node, int defaultValue) {
      return this.getGroupInfoInteger(world.getName(), group, node, defaultValue);
   }

   public abstract void setGroupInfoInteger(String var1, String var2, String var3, int var4);

   public void setGroupInfoInteger(World world, String group, String node, int value) {
      this.setGroupInfoInteger(world.getName(), group, node, value);
   }

   public double getPlayerInfoDouble(String world, OfflinePlayer player, String node, double defaultValue) {
      return this.getPlayerInfoDouble(world, player.getName(), node, defaultValue);
   }

   /** @deprecated */
   @Deprecated
   public abstract double getPlayerInfoDouble(String var1, String var2, String var3, double var4);

   /** @deprecated */
   @Deprecated
   public double getPlayerInfoDouble(World world, String player, String node, double defaultValue) {
      return this.getPlayerInfoDouble(world.getName(), player, node, defaultValue);
   }

   public double getPlayerInfoDouble(Player player, String node, double defaultValue) {
      return this.getPlayerInfoDouble((String)player.getWorld().getName(), (OfflinePlayer)player, node, defaultValue);
   }

   public void setPlayerInfoDouble(String world, OfflinePlayer player, String node, double value) {
      this.setPlayerInfoDouble(world, player.getName(), node, value);
   }

   /** @deprecated */
   @Deprecated
   public abstract void setPlayerInfoDouble(String var1, String var2, String var3, double var4);

   /** @deprecated */
   @Deprecated
   public void setPlayerInfoDouble(World world, String player, String node, double value) {
      this.setPlayerInfoDouble(world.getName(), player, node, value);
   }

   public void setPlayerInfoDouble(Player player, String node, double value) {
      this.setPlayerInfoDouble((String)player.getWorld().getName(), (OfflinePlayer)player, node, value);
   }

   public abstract double getGroupInfoDouble(String var1, String var2, String var3, double var4);

   public double getGroupInfoDouble(World world, String group, String node, double defaultValue) {
      return this.getGroupInfoDouble(world.getName(), group, node, defaultValue);
   }

   public abstract void setGroupInfoDouble(String var1, String var2, String var3, double var4);

   public void setGroupInfoDouble(World world, String group, String node, double value) {
      this.setGroupInfoDouble(world.getName(), group, node, value);
   }

   public boolean getPlayerInfoBoolean(String world, OfflinePlayer player, String node, boolean defaultValue) {
      return this.getPlayerInfoBoolean(world, player.getName(), node, defaultValue);
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean getPlayerInfoBoolean(String var1, String var2, String var3, boolean var4);

   /** @deprecated */
   @Deprecated
   public boolean getPlayerInfoBoolean(World world, String player, String node, boolean defaultValue) {
      return this.getPlayerInfoBoolean(world.getName(), player, node, defaultValue);
   }

   public boolean getPlayerInfoBoolean(Player player, String node, boolean defaultValue) {
      return this.getPlayerInfoBoolean((String)player.getWorld().getName(), (OfflinePlayer)player, node, defaultValue);
   }

   public void setPlayerInfoBoolean(String world, OfflinePlayer player, String node, boolean value) {
      this.setPlayerInfoBoolean(world, player.getName(), node, value);
   }

   /** @deprecated */
   @Deprecated
   public abstract void setPlayerInfoBoolean(String var1, String var2, String var3, boolean var4);

   /** @deprecated */
   @Deprecated
   public void setPlayerInfoBoolean(World world, String player, String node, boolean value) {
      this.setPlayerInfoBoolean(world.getName(), player, node, value);
   }

   public void setPlayerInfoBoolean(Player player, String node, boolean value) {
      this.setPlayerInfoBoolean((String)player.getWorld().getName(), (OfflinePlayer)player, node, value);
   }

   public abstract boolean getGroupInfoBoolean(String var1, String var2, String var3, boolean var4);

   public boolean getGroupInfoBoolean(World world, String group, String node, boolean defaultValue) {
      return this.getGroupInfoBoolean(world.getName(), group, node, defaultValue);
   }

   public abstract void setGroupInfoBoolean(String var1, String var2, String var3, boolean var4);

   public void setGroupInfoBoolean(World world, String group, String node, boolean value) {
      this.setGroupInfoBoolean(world.getName(), group, node, value);
   }

   public String getPlayerInfoString(String world, OfflinePlayer player, String node, String defaultValue) {
      return this.getPlayerInfoString(world, player.getName(), node, defaultValue);
   }

   /** @deprecated */
   @Deprecated
   public abstract String getPlayerInfoString(String var1, String var2, String var3, String var4);

   /** @deprecated */
   @Deprecated
   public String getPlayerInfoString(World world, String player, String node, String defaultValue) {
      return this.getPlayerInfoString(world.getName(), player, node, defaultValue);
   }

   public String getPlayerInfoString(Player player, String node, String defaultValue) {
      return this.getPlayerInfoString((String)player.getWorld().getName(), (OfflinePlayer)player, node, defaultValue);
   }

   public void setPlayerInfoString(String world, OfflinePlayer player, String node, String value) {
      this.setPlayerInfoString(world, player.getName(), node, value);
   }

   /** @deprecated */
   @Deprecated
   public abstract void setPlayerInfoString(String var1, String var2, String var3, String var4);

   /** @deprecated */
   @Deprecated
   public void setPlayerInfoString(World world, String player, String node, String value) {
      this.setPlayerInfoString(world.getName(), player, node, value);
   }

   public void setPlayerInfoString(Player player, String node, String value) {
      this.setPlayerInfoString((String)player.getWorld().getName(), (OfflinePlayer)player, node, value);
   }

   public abstract String getGroupInfoString(String var1, String var2, String var3, String var4);

   public String getGroupInfoString(World world, String group, String node, String defaultValue) {
      return this.getGroupInfoString(world.getName(), group, node, defaultValue);
   }

   public abstract void setGroupInfoString(String var1, String var2, String var3, String var4);

   public void setGroupInfoString(World world, String group, String node, String value) {
      this.setGroupInfoString(world.getName(), group, node, value);
   }

   public boolean playerInGroup(String world, OfflinePlayer player, String group) {
      return this.perms.playerInGroup(world, player, group);
   }

   /** @deprecated */
   @Deprecated
   public boolean playerInGroup(String world, String player, String group) {
      return this.perms.playerInGroup(world, player, group);
   }

   /** @deprecated */
   @Deprecated
   public boolean playerInGroup(World world, String player, String group) {
      return this.playerInGroup(world.getName(), player, group);
   }

   public boolean playerInGroup(Player player, String group) {
      return this.playerInGroup((String)player.getWorld().getName(), (OfflinePlayer)player, group);
   }

   public String[] getPlayerGroups(String world, OfflinePlayer player) {
      return this.perms.getPlayerGroups(world, player);
   }

   /** @deprecated */
   @Deprecated
   public String[] getPlayerGroups(String world, String player) {
      return this.perms.getPlayerGroups(world, player);
   }

   /** @deprecated */
   @Deprecated
   public String[] getPlayerGroups(World world, String player) {
      return this.getPlayerGroups(world.getName(), player);
   }

   public String[] getPlayerGroups(Player player) {
      return this.getPlayerGroups((String)player.getWorld().getName(), (OfflinePlayer)player);
   }

   public String getPrimaryGroup(String world, OfflinePlayer player) {
      return this.perms.getPrimaryGroup(world, player);
   }

   /** @deprecated */
   @Deprecated
   public String getPrimaryGroup(String world, String player) {
      return this.perms.getPrimaryGroup(world, player);
   }

   /** @deprecated */
   @Deprecated
   public String getPrimaryGroup(World world, String player) {
      return this.getPrimaryGroup(world.getName(), player);
   }

   public String getPrimaryGroup(Player player) {
      return this.getPrimaryGroup((String)player.getWorld().getName(), (OfflinePlayer)player);
   }

   public String[] getGroups() {
      return this.perms.getGroups();
   }
}
