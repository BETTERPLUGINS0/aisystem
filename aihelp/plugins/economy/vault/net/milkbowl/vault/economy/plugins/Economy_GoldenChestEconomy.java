package net.milkbowl.vault.economy.plugins;

import java.util.List;
import java.util.logging.Logger;
import me.igwb.GoldenChest.GoldenChestEconomy;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Economy_GoldenChestEconomy extends AbstractEconomy {
   private final Logger log;
   private final String name = "GoldenChestEconomy";
   private Plugin plugin = null;
   private GoldenChestEconomy economy = null;

   public Economy_GoldenChestEconomy(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_GoldenChestEconomy.EconomyServerListener(this), plugin);
      if (this.economy == null) {
         Plugin ec = plugin.getServer().getPluginManager().getPlugin("GoldenChestEconomy");
         if (ec != null && ec.isEnabled() && ec.getClass().getName().equals("me.igwb.GoldenChest.GoldenChestEconomy")) {
            this.economy = (GoldenChestEconomy)ec;
            this.log.info(String.format("[Economy] %s hooked.", "GoldenChestEconomy"));
         }
      }

   }

   public boolean isEnabled() {
      return this.economy == null ? false : this.economy.isEnabled();
   }

   public String getName() {
      return "GoldenChestEconomy";
   }

   public boolean hasBankSupport() {
      return false;
   }

   public int fractionalDigits() {
      return this.economy.getVaultConnector().fractionalDigits();
   }

   public String format(double amount) {
      return this.economy.getVaultConnector().format(amount);
   }

   public String currencyNamePlural() {
      return this.economy.getVaultConnector().currencyNamePlural();
   }

   public String currencyNameSingular() {
      return this.economy.getVaultConnector().currencyNameSingular();
   }

   public boolean hasAccount(String playerName) {
      return this.economy.getVaultConnector().hasAccount(playerName);
   }

   public boolean hasAccount(String playerName, String worldName) {
      return this.economy.getVaultConnector().hasAccount(playerName, worldName);
   }

   public double getBalance(String playerName) {
      return this.economy.getVaultConnector().getBalance(playerName);
   }

   public double getBalance(String playerName, String world) {
      return this.economy.getVaultConnector().getBalance(playerName, world);
   }

   public boolean has(String playerName, double amount) {
      return this.economy.getVaultConnector().has(playerName, amount);
   }

   public boolean has(String playerName, String worldName, double amount) {
      return this.economy.getVaultConnector().has(playerName, worldName, amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
      } else if (this.has(playerName, amount)) {
         this.economy.getVaultConnector().withdrawPlayer(playerName, amount);
         return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, (String)null);
      } else {
         return new EconomyResponse(0.0D, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
      }
   }

   public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
      return this.withdrawPlayer(playerName, amount);
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds");
      } else {
         this.economy.getVaultConnector().depositPlayer(playerName, amount);
         return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, (String)null);
      }
   }

   public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
      return this.depositPlayer(playerName, amount);
   }

   public EconomyResponse createBank(String name, String player) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse deleteBank(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse bankBalance(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse bankHas(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported!");
   }

   public List<String> getBanks() {
      return null;
   }

   public boolean createPlayerAccount(String playerName) {
      return this.economy.getVaultConnector().createPlayerAccount(playerName);
   }

   public boolean createPlayerAccount(String playerName, String worldName) {
      return this.economy.getVaultConnector().createPlayerAccount(playerName, worldName);
   }

   public class EconomyServerListener implements Listener {
      Economy_GoldenChestEconomy economy = null;

      public EconomyServerListener(Economy_GoldenChestEconomy economy_GoldenChestEconomy) {
         this.economy = economy_GoldenChestEconomy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin ec = event.getPlugin();
            if (ec.getDescription().getName().equals("GoldenChestEconomy") && ec.getClass().getName().equals("me.igwb.GoldenChest.GoldenChestEconomy")) {
               this.economy.economy = (GoldenChestEconomy)ec;
               Economy_GoldenChestEconomy.this.log.info(String.format("[Economy] %s hooked.", "GoldenChestEconomy"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("GoldenChestEconomy")) {
            this.economy.economy = null;
            Economy_GoldenChestEconomy.this.log.info(String.format("[Economy] %s unhooked.", "GoldenChestEconomy"));
         }

      }
   }
}
