package tntrun.arena.handlers;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import tntrun.TNTRun;

public class VaultHandler {
   private Economy economy;
   private Permission permission;
   private Chat chat;
   private final TNTRun plugin;

   public VaultHandler(TNTRun plugin) {
      this.plugin = plugin;
      Plugin Vault = plugin.getServer().getPluginManager().getPlugin("Vault");
      if (Vault != null) {
         plugin.getLogger().info("Successfully linked with Vault, version " + Vault.getDescription().getVersion());
         this.setupVaultEconomy();
         this.setupVaultPermissions();
         this.setupVaultChat();
      } else {
         plugin.getLogger().info("Vault plugin not found, economy disabled");
         this.economy = null;
         this.permission = null;
      }
   }

   private void setupVaultEconomy() {
      RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
      if (rsp != null) {
         this.plugin.getLogger().info("Vault: economy enabled.");
         this.economy = (Economy)rsp.getProvider();
      } else {
         this.plugin.getLogger().info("Vault: economy not detected.");
         this.economy = null;
      }

   }

   public boolean isEnabled() {
      return this.economy != null;
   }

   public Economy getEconomy() {
      return this.economy;
   }

   private void setupVaultPermissions() {
      RegisteredServiceProvider<Permission> rsp = this.plugin.getServer().getServicesManager().getRegistration(Permission.class);
      if (rsp != null) {
         this.permission = (Permission)rsp.getProvider();
      } else {
         this.plugin.getLogger().info("Vault: permission plugin not detected.");
         this.permission = null;
      }

   }

   public Permission getPermissions() {
      return this.permission;
   }

   public boolean isPermissions() {
      return this.permission != null;
   }

   private void setupVaultChat() {
      RegisteredServiceProvider<Chat> rsp = this.plugin.getServer().getServicesManager().getRegistration(Chat.class);
      if (rsp != null) {
         this.chat = (Chat)rsp.getProvider();
      } else {
         this.plugin.getLogger().info("Vault: chat plugin not detected.");
         this.chat = null;
      }

   }

   public Chat getChat() {
      return this.chat;
   }

   public boolean isChat() {
      return this.chat != null;
   }
}
