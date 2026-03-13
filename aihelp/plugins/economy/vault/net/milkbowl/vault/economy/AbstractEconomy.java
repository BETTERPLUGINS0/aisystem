package net.milkbowl.vault.economy;

import org.bukkit.OfflinePlayer;

public abstract class AbstractEconomy implements Economy {
   public boolean hasAccount(OfflinePlayer player) {
      return player.getName() == null ? false : this.hasAccount(player.getName());
   }

   public boolean hasAccount(OfflinePlayer player, String worldName) {
      return player.getName() == null ? false : this.hasAccount(player.getName(), worldName);
   }

   public double getBalance(OfflinePlayer player) {
      return this.getBalance(player.getName());
   }

   public double getBalance(OfflinePlayer player, String world) {
      return this.getBalance(player.getName(), world);
   }

   public boolean has(OfflinePlayer player, double amount) {
      return player.getName() == null ? false : this.has(player.getName(), amount);
   }

   public boolean has(OfflinePlayer player, String worldName, double amount) {
      return player.getName() == null ? false : this.has(player.getName(), worldName, amount);
   }

   public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
      return this.withdrawPlayer(player.getName(), amount);
   }

   public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
      return this.withdrawPlayer(player.getName(), worldName, amount);
   }

   public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
      return this.depositPlayer(player.getName(), amount);
   }

   public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
      return this.depositPlayer(player.getName(), worldName, amount);
   }

   public EconomyResponse createBank(String name, OfflinePlayer player) {
      return this.createBank(name, player.getName());
   }

   public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
      return this.isBankOwner(name, player.getName());
   }

   public EconomyResponse isBankMember(String name, OfflinePlayer player) {
      return this.isBankMember(name, player.getName());
   }

   public boolean createPlayerAccount(OfflinePlayer player) {
      return this.createPlayerAccount(player.getName());
   }

   public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
      return this.createPlayerAccount(player.getName(), worldName);
   }
}
