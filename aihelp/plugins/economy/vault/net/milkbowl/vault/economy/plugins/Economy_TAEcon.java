package net.milkbowl.vault.economy.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teamalpha.taecon.TAEcon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Economy_TAEcon extends AbstractEconomy {
   private final Logger log;
   private final String name = "TAEcon";
   private Plugin plugin = null;
   private TAEcon economy = null;

   public Economy_TAEcon(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_TAEcon.EconomyServerListener(this), plugin);
      if (this.economy == null) {
         Plugin taecon = plugin.getServer().getPluginManager().getPlugin("TAEcon");
         if (taecon != null && taecon.isEnabled()) {
            this.economy = (TAEcon)taecon;
            this.log.info(String.format("[Economy] %s hooked.", "TAEcon"));
         }
      }

   }

   public boolean isEnabled() {
      return this.economy != null;
   }

   public String getName() {
      return "TAEcon";
   }

   public boolean hasBankSupport() {
      return false;
   }

   public int fractionalDigits() {
      return 0;
   }

   public String format(double amount) {
      amount = Math.ceil(amount);
      return amount == 1.0D ? String.format("%d %s", (int)amount, this.currencyNameSingular()) : String.format("%d %s", (int)amount, this.currencyNamePlural());
   }

   public String currencyNamePlural() {
      return this.economy.getCurrencyName(true);
   }

   public String currencyNameSingular() {
      return this.economy.getCurrencyName(false);
   }

   public boolean hasAccount(String playerName) {
      return true;
   }

   public double getBalance(String playerName) {
      return (double)this.economy.getBalance(playerName);
   }

   public boolean has(String playerName, double amount) {
      return this.getBalance(playerName) >= amount;
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      int iamount = (int)Math.ceil(amount);
      EconomyResponse.ResponseType rt;
      String message;
      if (this.has(playerName, amount)) {
         if (this.economy.removeBalance(playerName, iamount)) {
            rt = EconomyResponse.ResponseType.SUCCESS;
            message = null;
         } else {
            rt = EconomyResponse.ResponseType.SUCCESS;
            message = "ERROR";
         }
      } else {
         rt = EconomyResponse.ResponseType.FAILURE;
         message = "Not enough money";
      }

      return new EconomyResponse((double)iamount, this.getBalance(playerName), rt, message);
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      int iamount = (int)Math.floor(amount);
      EconomyResponse.ResponseType rt;
      String message;
      if (this.economy.addBalance(playerName, iamount)) {
         rt = EconomyResponse.ResponseType.SUCCESS;
         message = null;
      } else {
         rt = EconomyResponse.ResponseType.SUCCESS;
         message = "ERROR";
      }

      return new EconomyResponse((double)iamount, this.getBalance(playerName), rt, message);
   }

   public EconomyResponse createBank(String name, String player) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse deleteBank(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse bankBalance(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse bankHas(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "TAEcon does not support bank accounts");
   }

   public List<String> getBanks() {
      return new ArrayList();
   }

   public boolean createPlayerAccount(String playerName) {
      return false;
   }

   public boolean hasAccount(String playerName, String worldName) {
      return true;
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
      return false;
   }

   public class EconomyServerListener implements Listener {
      Economy_TAEcon economy = null;

      public EconomyServerListener(Economy_TAEcon economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin taecon = event.getPlugin();
            if (taecon.getDescription().getName().equals("TAEcon")) {
               this.economy.economy = (TAEcon)taecon;
               Economy_TAEcon.this.log.info(String.format("[Economy] %s hooked.", "TAEcon"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("TAEcon")) {
            this.economy.economy = null;
            Economy_TAEcon.this.log.info(String.format("[Economy] %s unhooked.", "TAEcon"));
         }

      }
   }
}
