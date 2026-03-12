package net.milkbowl.vault.economy.plugins;

import com.gmail.bleedobsidian.miconomy.Main;
import com.gmail.bleedobsidian.miconomy.MiConomy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Economy_MiConomy extends AbstractEconomy {
   private final Logger log;
   private final String name = "MiConomy";
   private Plugin plugin;
   private MiConomy economy;
   private Main miConomy;

   public Economy_MiConomy(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_MiConomy.EconomyServerListener(this), plugin);
      if (this.miConomy == null) {
         Plugin miConomyPlugin = plugin.getServer().getPluginManager().getPlugin("MiConomy");
         if (this.miConomy != null) {
            this.miConomy = (Main)miConomyPlugin;
            this.economy = this.miConomy.getInstance();
            this.log.info(String.format("[Economy] %s hooked.", "MiConomy"));
         }
      }

   }

   public boolean isEnabled() {
      return this.miConomy == null ? false : this.miConomy.isEnabled();
   }

   public String getName() {
      return "MiConomy";
   }

   public boolean hasBankSupport() {
      return true;
   }

   public int fractionalDigits() {
      return 2;
   }

   public String format(double amount) {
      return this.economy.getFormattedValue(amount);
   }

   public String currencyNamePlural() {
      return this.miConomy.getPluginConfig().MoneyNamePlural;
   }

   public String currencyNameSingular() {
      return this.miConomy.getPluginConfig().MoneyName;
   }

   public boolean hasAccount(String playerName) {
      List<World> worlds = this.plugin.getServer().getWorlds();
      return this.hasAccount(playerName, ((World)worlds.get(0)).getName());
   }

   public boolean hasAccount(String playerName, String worldName) {
      OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerName);
      World world = this.plugin.getServer().getWorld(worldName);
      return this.economy.isAccountCreated(player, world);
   }

   public double getBalance(String playerName) {
      List<World> worlds = this.plugin.getServer().getWorlds();
      return this.getBalance(playerName, ((World)worlds.get(0)).getName());
   }

   public double getBalance(String playerName, String worldName) {
      OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerName);
      World world = this.plugin.getServer().getWorld(worldName);
      return this.economy.getAccountBalance(player, world);
   }

   public boolean has(String playerName, double amount) {
      List<World> worlds = this.plugin.getServer().getWorlds();
      return this.has(playerName, ((World)worlds.get(0)).getName(), amount);
   }

   public boolean has(String playerName, String worldName, double amount) {
      OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerName);
      World world = this.plugin.getServer().getWorld(worldName);
      double playerBalance = this.economy.getAccountBalance(player, world);
      return playerBalance >= amount;
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      List<World> worlds = this.plugin.getServer().getWorlds();
      return this.withdrawPlayer(playerName, ((World)worlds.get(0)).getName(), amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
      OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerName);
      World world = this.plugin.getServer().getWorld(worldName);
      double balance = this.economy.getAccountBalance(player, world);
      if (this.getBalance(playerName, worldName) < amount) {
         return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
      } else if (this.economy.removeAccountBalance(player, amount, world)) {
         balance = this.economy.getAccountBalance(player, world);
         return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "Failed to remove funds from account");
      }
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      List<World> worlds = this.plugin.getServer().getWorlds();
      return this.depositPlayer(playerName, ((World)worlds.get(0)).getName(), amount);
   }

   public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
      OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerName);
      World world = this.plugin.getServer().getWorld(worldName);
      double balance = this.economy.getAccountBalance(player, world);
      if (this.economy.addAccountBalance(player, amount, world)) {
         balance = this.economy.getAccountBalance(player, world);
         return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "Failed to add funds to account");
      }
   }

   public EconomyResponse createBank(String name, String player) {
      OfflinePlayer owner = this.plugin.getServer().getOfflinePlayer(player);
      ArrayList<OfflinePlayer> owners = new ArrayList();
      owners.add(owner);
      if (!this.economy.isBankCreated(name)) {
         this.economy.createBank(name, owners, new ArrayList(), false);
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "A bank with this name already exists");
      }
   }

   public EconomyResponse deleteBank(String name) {
      if (this.economy.isBankCreated(name)) {
         this.economy.deleteBank(name);
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public EconomyResponse bankBalance(String name) {
      if (this.economy.isBankCreated(name)) {
         double balance = this.economy.getBankBalance(name);
         return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public EconomyResponse bankHas(String name, double amount) {
      if (this.economy.isBankCreated(name)) {
         double balance = this.economy.getBankBalance(name);
         return balance >= amount ? new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.SUCCESS, "") : new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "The bank does not have enough money!");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      if (this.economy.isBankCreated(name)) {
         this.economy.removeBankBalance(name, amount);
         double balance = this.economy.getBankBalance(name);
         return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      if (this.economy.isBankCreated(name)) {
         this.economy.addBankBalance(name, amount);
         double balance = this.economy.getBankBalance(name);
         return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      OfflinePlayer owner = this.plugin.getServer().getOfflinePlayer(playerName);
      if (this.economy.isBankCreated(name)) {
         return this.economy.isPlayerBankOwner(name, owner) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "") : new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "The player is not a bank owner");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      OfflinePlayer owner = this.plugin.getServer().getOfflinePlayer(playerName);
      if (this.economy.isBankCreated(name)) {
         return this.economy.isPlayerBankMember(name, owner) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "") : new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "The player is not a bank member");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank doesn't exist");
      }
   }

   public List<String> getBanks() {
      return this.economy.getBanks();
   }

   public boolean createPlayerAccount(String playerName) {
      List<World> worlds = this.plugin.getServer().getWorlds();
      return this.createPlayerAccount(playerName, ((World)worlds.get(0)).getName());
   }

   public boolean createPlayerAccount(String playerName, String worldName) {
      OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerName);
      World world = this.plugin.getServer().getWorld(worldName);
      if (!this.economy.isAccountCreated(player, world)) {
         this.economy.createAccount(player, 0.0D, world);
         return true;
      } else {
         return false;
      }
   }

   public class EconomyServerListener implements Listener {
      Economy_MiConomy economy = null;

      public EconomyServerListener(Economy_MiConomy economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin miConomyPlugin = event.getPlugin();
            if (miConomyPlugin.getDescription().getName().equals("MiConomy")) {
               this.economy.miConomy = (Main)miConomyPlugin;
               this.economy.economy = Economy_MiConomy.this.miConomy.getInstance();
               Economy_MiConomy.this.log.info(String.format("[Economy] %s hooked.", "MiConomy"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("MiConomy")) {
            this.economy.miConomy = null;
            this.economy.economy = null;
            Economy_MiConomy.this.log.info(String.format("[Economy] %s unhooked.", "MiConomy"));
         }

      }
   }
}
