package net.milkbowl.vault.economy.plugins;

import java.util.List;
import java.util.logging.Logger;
import me.coniin.plugins.minefaconomy.Minefaconomy;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Economy_Minefaconomy extends AbstractEconomy {
   private final Logger log;
   private final String name = "Minefaconomy";
   private Plugin plugin = null;
   private Minefaconomy economy = null;

   public Economy_Minefaconomy(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_Minefaconomy.EconomyServerListener(this), plugin);
      Plugin econ = null;
      if (this.economy == null) {
         econ = plugin.getServer().getPluginManager().getPlugin("Minefaconomy");
         this.log.info("Loading Minefaconomy");
      }

      if (econ != null && econ.isEnabled()) {
         this.economy = (Minefaconomy)econ;
         Logger var10000 = this.log;
         Object[] var10002 = new Object[1];
         this.getClass();
         var10002[0] = "Minefaconomy";
         var10000.info(String.format("[Economy] %s hooked.", var10002));
      } else {
         this.log.info("Error Loading Minefaconomy");
      }
   }

   public boolean isEnabled() {
      return this.economy != null && this.economy.isEnabled();
   }

   public String getName() {
      return "Minefaconomy";
   }

   public int fractionalDigits() {
      return Minefaconomy.vaultLayer.fractionalDigits();
   }

   public String format(double amount) {
      return Minefaconomy.vaultLayer.format(amount);
   }

   public String currencyNamePlural() {
      return Minefaconomy.vaultLayer.currencyNamePlural();
   }

   public String currencyNameSingular() {
      return Minefaconomy.vaultLayer.currencyNameSingular();
   }

   public boolean hasAccount(String playerName) {
      return Minefaconomy.vaultLayer.hasAccount(playerName);
   }

   public boolean hasAccount(String playerName, String worldName) {
      return Minefaconomy.vaultLayer.hasAccount(playerName);
   }

   public double getBalance(String playerName) {
      return Minefaconomy.vaultLayer.getBalance(playerName);
   }

   public double getBalance(String playerName, String world) {
      return Minefaconomy.vaultLayer.getBalance(playerName);
   }

   public boolean has(String playerName, double amount) {
      return Minefaconomy.vaultLayer.has(playerName, amount);
   }

   public boolean has(String playerName, String worldName, double amount) {
      return Minefaconomy.vaultLayer.has(playerName, amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      return Minefaconomy.vaultLayer.withdrawPlayer(playerName, amount);
   }

   public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
      return Minefaconomy.vaultLayer.withdrawPlayer(playerName, amount);
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      return Minefaconomy.vaultLayer.depositPlayer(playerName, amount);
   }

   public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
      return Minefaconomy.vaultLayer.depositPlayer(playerName, amount);
   }

   public boolean createPlayerAccount(String playerName) {
      return Minefaconomy.vaultLayer.createPlayerAccount(playerName);
   }

   public boolean createPlayerAccount(String playerName, String worldName) {
      return Minefaconomy.vaultLayer.createPlayerAccount(playerName);
   }

   public boolean hasBankSupport() {
      return Minefaconomy.vaultLayer.hasBankSupport();
   }

   public EconomyResponse createBank(String name, String player) {
      return Minefaconomy.vaultLayer.createBank(name, player);
   }

   public EconomyResponse deleteBank(String name) {
      return Minefaconomy.vaultLayer.deleteBank(name);
   }

   public EconomyResponse bankBalance(String name) {
      return Minefaconomy.vaultLayer.bankBalance(name);
   }

   public EconomyResponse bankHas(String name, double amount) {
      return Minefaconomy.vaultLayer.bankHas(name, amount);
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return Minefaconomy.vaultLayer.bankWithdraw(name, amount);
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return Minefaconomy.vaultLayer.bankDeposit(name, amount);
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return Minefaconomy.vaultLayer.isBankOwner(name, playerName);
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return Minefaconomy.vaultLayer.isBankMember(name, playerName);
   }

   public List<String> getBanks() {
      return Minefaconomy.vaultLayer.getBanks();
   }

   public class EconomyServerListener implements Listener {
      Economy_Minefaconomy economy_minefaconomy = null;

      public EconomyServerListener(Economy_Minefaconomy economy_minefaconomy) {
         this.economy_minefaconomy = economy_minefaconomy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy_minefaconomy.economy == null) {
            Plugin mfc = event.getPlugin();
            if (mfc.getDescription().getName().equals("Minefaconomy")) {
               this.economy_minefaconomy.economy = Economy_Minefaconomy.this.economy;
               Economy_Minefaconomy.this.log.info(String.format("[Economy] %s hooked.", "Minefaconomy"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy_minefaconomy.economy != null && event.getPlugin().getDescription().getName().equals("Minefaconomy")) {
            this.economy_minefaconomy.economy = null;
            Economy_Minefaconomy.this.log.info(String.format("[Economy] %s unhooked.", "Minefaconomy"));
         }

      }
   }
}
