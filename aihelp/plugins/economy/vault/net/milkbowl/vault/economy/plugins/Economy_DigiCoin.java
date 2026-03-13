package net.milkbowl.vault.economy.plugins;

import co.uk.silvania.cities.digicoin.DigiCoin;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Economy_DigiCoin extends AbstractEconomy {
   private final Logger log;
   private final String name = "DigiCoin";
   private Plugin plugin = null;
   private DigiCoin economy = null;

   public Economy_DigiCoin(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_DigiCoin.EconomyServerListener(this), plugin);
      if (this.economy == null) {
         Plugin digicoin = plugin.getServer().getPluginManager().getPlugin("DigiCoin");
         if (digicoin != null && digicoin.isEnabled()) {
            this.economy = (DigiCoin)digicoin;
            this.log.info(String.format("[Economy] %s hooked.", "DigiCoin"));
         }
      }

   }

   public boolean isEnabled() {
      return this.economy != null;
   }

   public String getName() {
      return "DigiCoin";
   }

   public boolean hasBankSupport() {
      return false;
   }

   public int fractionalDigits() {
      return -1;
   }

   public String format(double amount) {
      return amount == 1.0D ? String.format("%d %s", amount, this.currencyNameSingular()) : String.format("%d %s", amount, this.currencyNamePlural());
   }

   public String currencyNamePlural() {
      return "coins";
   }

   public String currencyNameSingular() {
      return "coin";
   }

   public boolean hasAccount(String playerName) {
      return true;
   }

   public double getBalance(String playerName) {
      return this.economy.getBalance(playerName);
   }

   public boolean has(String playerName, double amount) {
      return this.getBalance(playerName) >= amount;
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      EconomyResponse.ResponseType rt;
      String message;
      if (this.economy.removeBalance(playerName, amount)) {
         rt = EconomyResponse.ResponseType.SUCCESS;
         message = null;
      } else {
         rt = EconomyResponse.ResponseType.FAILURE;
         message = "Not enough money.";
      }

      return new EconomyResponse(amount, this.getBalance(playerName), rt, message);
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      EconomyResponse.ResponseType rt;
      String message;
      if (this.economy.addBalance(playerName, amount)) {
         rt = EconomyResponse.ResponseType.SUCCESS;
         message = null;
      } else {
         rt = EconomyResponse.ResponseType.FAILURE;
         message = "Failed to deposit balance.";
      }

      return new EconomyResponse(amount, this.getBalance(playerName), rt, message);
   }

   public EconomyResponse createBank(String name, String player) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse deleteBank(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse bankBalance(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse bankHas(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "DigiCoin does not support bank accounts");
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
      Economy_DigiCoin economy = null;

      public EconomyServerListener(Economy_DigiCoin economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin digicoin = event.getPlugin();
            if (digicoin.getDescription().getName().equals("DigiCoin")) {
               this.economy.economy = (DigiCoin)digicoin;
               Economy_DigiCoin.this.log.info(String.format("[Economy] %s hooked.", "DigiCoin"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("DigiCoin")) {
            this.economy.economy = null;
            Economy_DigiCoin.this.log.info(String.format("[Economy] %s unhooked.", "DigiCoin"));
         }

      }
   }
}
