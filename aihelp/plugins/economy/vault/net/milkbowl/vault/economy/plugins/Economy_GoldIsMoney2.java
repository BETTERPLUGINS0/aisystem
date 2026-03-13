package net.milkbowl.vault.economy.plugins;

import com.flobi.GoldIsMoney2.GoldIsMoney;
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

public class Economy_GoldIsMoney2 extends AbstractEconomy {
   private final Logger log;
   private final String name = "GoldIsMoney";
   private Plugin plugin = null;
   protected GoldIsMoney economy = null;

   public Economy_GoldIsMoney2(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_GoldIsMoney2.EconomyServerListener(this), plugin);
      if (this.economy == null) {
         Plugin ec = plugin.getServer().getPluginManager().getPlugin("GoldIsMoney");
         if (ec != null && ec.isEnabled() && ec.getClass().getName().equals("com.flobi.GoldIsMoney2.GoldIsMoney")) {
            this.economy = (GoldIsMoney)ec;
            this.log.info(String.format("[Economy] %s hooked.", "GoldIsMoney"));
         }
      }

   }

   public boolean isEnabled() {
      return this.economy == null ? false : this.economy.isEnabled();
   }

   public String getName() {
      return "GoldIsMoney";
   }

   public boolean hasBankSupport() {
      return GoldIsMoney.hasBankSupport();
   }

   public int fractionalDigits() {
      return GoldIsMoney.fractionalDigits();
   }

   public String format(double amount) {
      return GoldIsMoney.format(amount);
   }

   public String currencyNamePlural() {
      return GoldIsMoney.currencyNamePlural();
   }

   public String currencyNameSingular() {
      return GoldIsMoney.currencyNameSingular();
   }

   public boolean hasAccount(String playerName) {
      return GoldIsMoney.hasAccount(playerName);
   }

   public double getBalance(String playerName) {
      return GoldIsMoney.getBalance(playerName);
   }

   public boolean has(String playerName, double amount) {
      return GoldIsMoney.has(playerName, amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
      } else if (!GoldIsMoney.hasAccount(playerName)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That player does not have an account!");
      } else if (!GoldIsMoney.has(playerName, amount)) {
         return new EconomyResponse(0.0D, GoldIsMoney.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
      } else {
         return !GoldIsMoney.withdrawPlayer(playerName, amount) ? new EconomyResponse(0.0D, GoldIsMoney.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Unable to withdraw funds!") : new EconomyResponse(amount, GoldIsMoney.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, (String)null);
      }
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds!");
      } else if (!GoldIsMoney.hasAccount(playerName)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That player does not have an account!");
      } else {
         return !GoldIsMoney.depositPlayer(playerName, amount) ? new EconomyResponse(0.0D, GoldIsMoney.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Unable to deposit funds!") : new EconomyResponse(amount, GoldIsMoney.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, (String)null);
      }
   }

   public EconomyResponse createBank(String name, String player) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else {
         return !GoldIsMoney.createBank(name, player) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Unable to create bank account.") : new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse deleteBank(String name) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else {
         return !GoldIsMoney.deleteBank(name) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Unable to remove bank account.") : new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse bankBalance(String name) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else {
         return !GoldIsMoney.bankExists(name) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!") : new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse bankHas(String name, double amount) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else if (!GoldIsMoney.bankExists(name)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
      } else {
         return GoldIsMoney.bankHas(name, amount) ? new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.FAILURE, "The bank does not have enough money!") : new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else if (!GoldIsMoney.bankExists(name)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
      } else if (!GoldIsMoney.bankHas(name, amount)) {
         return new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.FAILURE, "The bank does not have enough money!");
      } else {
         return !GoldIsMoney.bankWithdraw(name, amount) ? new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.FAILURE, "Unable to withdraw from that bank account!") : new EconomyResponse(amount, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else if (!GoldIsMoney.bankExists(name)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
      } else {
         return !GoldIsMoney.bankDeposit(name, amount) ? new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.FAILURE, "Unable to deposit to that bank account!") : new EconomyResponse(amount, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else if (!GoldIsMoney.bankExists(name)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
      } else {
         return !GoldIsMoney.isBankOwner(name, playerName) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That player does not own that bank!") : new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      if (!GoldIsMoney.hasBankSupport()) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GoldIsMoney bank support is disabled!");
      } else if (!GoldIsMoney.bankExists(name)) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
      } else {
         return !GoldIsMoney.isBankMember(name, playerName) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That player is not a member of that bank!") : new EconomyResponse(0.0D, GoldIsMoney.bankBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
      }
   }

   public List<String> getBanks() {
      return GoldIsMoney.getBanks();
   }

   public boolean createPlayerAccount(String playerName) {
      return GoldIsMoney.createPlayerAccount(playerName);
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
      Economy_GoldIsMoney2 economy = null;

      public EconomyServerListener(Economy_GoldIsMoney2 economy_GoldIsMoney2) {
         this.economy = economy_GoldIsMoney2;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin ec = event.getPlugin();
            if (ec.getClass().getName().equals("com.flobi.GoldIsMoney2.GoldIsMoney")) {
               this.economy.economy = (GoldIsMoney)ec;
               Economy_GoldIsMoney2.this.log.info(String.format("[Economy] %s hooked.", "GoldIsMoney"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("GoldIsMoney")) {
            this.economy.economy = null;
            Economy_GoldIsMoney2.this.log.info(String.format("[Economy] %s unhooked.", "GoldIsMoney"));
         }

      }
   }
}
