package net.milkbowl.vault.economy.plugins;

import ca.agnate.EconXP.EconXP;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Economy_EconXP extends AbstractEconomy {
   private final Logger log;
   private final String name = "EconXP";
   private Plugin plugin = null;
   private EconXP econ = null;

   public Economy_EconXP(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_EconXP.EconomyServerListener(this), plugin);
      this.log.log(Level.WARNING, "EconXP is an integer only economy, you may notice inconsistencies with accounts if you do not setup your other econ using plugins accordingly!");
      if (this.econ == null) {
         Plugin econ = plugin.getServer().getPluginManager().getPlugin("EconXP");
         if (econ != null && econ.isEnabled()) {
            this.econ = (EconXP)econ;
            this.log.info(String.format("[Economy] %s hooked.", "EconXP"));
         }
      }

   }

   public boolean isEnabled() {
      return this.econ != null;
   }

   public String getName() {
      return "EconXP";
   }

   public String format(double amount) {
      amount = Math.ceil(amount);
      return String.format("%d %s", (int)amount, "experience");
   }

   public String currencyNamePlural() {
      return "experience";
   }

   public String currencyNameSingular() {
      return "experience";
   }

   public double getBalance(String playerName) {
      OfflinePlayer player = this.econ.getPlayer(playerName);
      return player == null ? 0.0D : (double)this.econ.getExp(player);
   }

   public boolean has(String playerName, double amount) {
      OfflinePlayer player = this.econ.getPlayer(playerName);
      return player == null ? false : this.econ.hasExp(player, (int)Math.ceil(amount));
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      OfflinePlayer player = this.econ.getPlayer(playerName);
      if (player == null) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Player does not exist");
      } else {
         double balance = (double)this.econ.getExp(player);
         amount = Math.ceil(amount);
         if (amount < 0.0D) {
            return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
         } else if (!this.econ.hasExp(player, (int)amount)) {
            return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
         } else {
            this.econ.removeExp(player, (int)amount);
            double finalBalance = (double)this.econ.getExp(player);
            return new EconomyResponse(amount, finalBalance, EconomyResponse.ResponseType.SUCCESS, (String)null);
         }
      }
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      OfflinePlayer player = this.econ.getPlayer(playerName);
      if (player == null) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Player does not exist");
      } else {
         double balance = (double)this.econ.getExp(player);
         amount = Math.ceil(amount);
         if (amount < 0.0D) {
            return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
         } else {
            this.econ.addExp(player, (int)amount);
            balance = (double)this.econ.getExp(player);
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, (String)null);
         }
      }
   }

   public EconomyResponse createBank(String name, String player) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse deleteBank(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse bankHas(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public EconomyResponse bankBalance(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "EconXP does not support bank accounts!");
   }

   public List<String> getBanks() {
      return new ArrayList();
   }

   public boolean hasBankSupport() {
      return false;
   }

   public boolean hasAccount(String playerName) {
      return this.econ.getPlayer(playerName) != null;
   }

   public boolean createPlayerAccount(String playerName) {
      return false;
   }

   public int fractionalDigits() {
      return 0;
   }

   public boolean hasAccount(String playerName, String worldName) {
      return this.hasAccount(playerName);
   }

   public double getBalance(String playerName, String world) {
      return this.getBalance(playerName);
   }

   public boolean has(String playerName, String worldName, double amount) {
      return this.has(playerName, amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
      return this.withdrawPlayer(playerName, amount);
   }

   public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
      return this.depositPlayer(playerName, amount);
   }

   public boolean createPlayerAccount(String playerName, String worldName) {
      return this.createPlayerAccount(playerName);
   }

   public class EconomyServerListener implements Listener {
      Economy_EconXP economy = null;

      public EconomyServerListener(Economy_EconXP economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.econ == null) {
            Plugin eco = event.getPlugin();
            if (eco.getDescription().getName().equals("EconXP")) {
               this.economy.econ = (EconXP)eco;
               Economy_EconXP.this.log.info(String.format("[Economy] %s hooked.", "EconXP"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.econ != null && event.getPlugin().getDescription().getName().equals("EconXP")) {
            this.economy.econ = null;
            Economy_EconXP.this.log.info(String.format("[Economy] %s unhooked.", "EconXP"));
         }

      }
   }
}
