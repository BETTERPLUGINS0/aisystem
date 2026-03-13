package net.milkbowl.vault.economy.plugins;

import com.github.zathrus_writer.commandsex.CommandsEX;
import com.github.zathrus_writer.commandsex.api.economy.Economy;
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

public class Economy_CommandsEX extends AbstractEconomy {
   private final Logger log;
   private final String name = "CommandsEX Economy";
   private Plugin plugin = null;
   private CommandsEX economy = null;

   public Economy_CommandsEX(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_CommandsEX.EconomyServerListener(this), plugin);
      if (this.economy == null) {
         Plugin commandsex = plugin.getServer().getPluginManager().getPlugin("CommandsEX");
         if (commandsex != null && commandsex.isEnabled()) {
            this.economy = (CommandsEX)commandsex;
            this.log.info(String.format("[Economy] %s hooked.", "CommandsEX Economy"));
         }
      }

   }

   public boolean isEnabled() {
      return this.economy == null ? false : Economy.isEnabled();
   }

   public String getName() {
      return "CommandsEX Economy";
   }

   public boolean hasBankSupport() {
      return false;
   }

   public int fractionalDigits() {
      return 2;
   }

   public String format(double amount) {
      return Economy.getCurrencySymbol() + amount;
   }

   public String currencyNamePlural() {
      return Economy.getCurrencyPlural();
   }

   public String currencyNameSingular() {
      return Economy.getCurrencySingular();
   }

   public boolean hasAccount(String playerName) {
      return Economy.hasAccount(playerName);
   }

   public double getBalance(String playerName) {
      return Economy.getBalance(playerName);
   }

   public boolean has(String playerName, double amount) {
      return Economy.has(playerName, amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      EconomyResponse.ResponseType rt;
      String message;
      if (Economy.has(playerName, amount)) {
         Economy.withdraw(playerName, amount);
         rt = EconomyResponse.ResponseType.SUCCESS;
         message = null;
      } else {
         rt = EconomyResponse.ResponseType.FAILURE;
         message = "Not enough money";
      }

      return new EconomyResponse(amount, Economy.getBalance(playerName), rt, message);
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      Economy.deposit(playerName, amount);
      return new EconomyResponse(amount, Economy.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "Successfully deposited");
   }

   public EconomyResponse createBank(String name, String player) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse deleteBank(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse bankBalance(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse bankHas(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CommandsEX Economy does not support bank accounts");
   }

   public List<String> getBanks() {
      return new ArrayList();
   }

   public boolean createPlayerAccount(String playerName) {
      if (Economy.hasAccount(playerName)) {
         return false;
      } else {
         Economy.createAccount(playerName);
         return true;
      }
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
      Economy_CommandsEX economy = null;

      public EconomyServerListener(Economy_CommandsEX economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin cex = event.getPlugin();
            if (cex.getDescription().getName().equals("CommandsEX")) {
               this.economy.economy = (CommandsEX)cex;
               Economy_CommandsEX.this.log.info(String.format("[Economy] %s hooked.", "CommandsEX Economy"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("CommandsEX")) {
            this.economy.economy = null;
            Economy_CommandsEX.this.log.info(String.format("[Economy] %s unhooked.", "CommandsEX Economy"));
         }

      }
   }
}
