package com.badbones69.crazyauctions.api.enums.other;

import com.badbones69.crazyauctions.CrazyAuctions;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public enum Permissions {
   reload("reload", "Access to /crazyauctions reload", PermissionDefault.OP, false),
   help("help", "Access to /crazyauctions help", PermissionDefault.TRUE, false),
   view("view", "Access to /crazyauctions view", PermissionDefault.TRUE, false),
   expired("expired", "Access to /crazyauctions expired", PermissionDefault.TRUE, false),
   listed("listed", "Access to /crazyauctions listed", PermissionDefault.TRUE, false),
   sell("sell", "Access to /crazyauctions sell", PermissionDefault.TRUE, false),
   bid("bid", "Access to /crazyauctions bid", PermissionDefault.TRUE, false),
   use("use", "Access to /crazyauctions", PermissionDefault.TRUE, false),
   bypass("bypass", "Bypasses most plugin restrictions", PermissionDefault.OP, true),
   access("access", "Access other portions of the plugin", PermissionDefault.TRUE, true),
   player_wildcard("player.*", "Access multiple player based commands", PermissionDefault.FALSE, new HashMap<String, Boolean>() {
      {
         this.put("crazyauctions.bid", true);
         this.put("crazyauctions.sell", true);
         this.put("crazyauctions.access", true);
         this.put("crazyauctions.view", true);
      }
   }, true),
   admin_wildcard("admin", "Access multiple admin based commands", PermissionDefault.FALSE, new HashMap<String, Boolean>() {
      {
         this.put("crazyauctions.player.*", true);
         this.put("crazyauctions.reload", true);
         this.put("crazyauctions.bypass", true);
         this.put("crazyauctions.view", true);
      }
   }, true);

   private final String node;
   private final String description;
   private final PermissionDefault isDefault;
   private final Map<String, Boolean> children;
   private final boolean register;
   private final PluginManager manager = CrazyAuctions.get().getServer().getPluginManager();

   private Permissions(String param3, String param4, PermissionDefault param5, Map<String, Boolean> param6, boolean param7) {
      this.node = node;
      this.description = description;
      this.isDefault = isDefault;
      this.children = children;
      this.register = register;
   }

   private Permissions(String param3, String param4, PermissionDefault param5, boolean param6) {
      this.node = node;
      this.description = description;
      this.isDefault = isDefault;
      this.children = new HashMap();
      this.register = register;
   }

   public final String getNode() {
      return "crazyauctions." + this.node;
   }

   public final boolean shouldRegister() {
      return this.register;
   }

   public final String getDescription() {
      return this.description;
   }

   public final PermissionDefault isDefault() {
      return this.isDefault;
   }

   public final Map<String, Boolean> getChildren() {
      return this.children;
   }

   public final boolean hasPermission(Player player) {
      return player.hasPermission(this.getNode());
   }

   public final boolean isValid() {
      return this.manager.getPermission(this.getNode()) != null;
   }

   public final Permission getPermission() {
      return new Permission(this.getNode(), this.getDescription(), this.isDefault(), this.getChildren().isEmpty() ? null : this.getChildren());
   }

   public void registerPermission() {
      if (!this.isValid()) {
         this.manager.addPermission(this.getPermission());
      }
   }

   public void unregisterPermission() {
      if (this.isValid()) {
         this.manager.removePermission(this.getNode());
      }
   }

   // $FF: synthetic method
   private static Permissions[] $values() {
      return new Permissions[]{reload, help, view, expired, listed, sell, bid, use, bypass, access, player_wildcard, admin_wildcard};
   }
}
