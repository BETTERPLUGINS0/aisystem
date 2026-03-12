package net.milkbowl.vault.economy.plugins;

import com.iCo6.iConomy;
import com.iCo6.Constants.Nodes;
import com.iCo6.system.Accounts;
import com.iCo6.system.Holdings;
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

public class Economy_iConomy6 extends AbstractEconomy {
   private final Logger log;
   private String name = "iConomy ";
   private Plugin plugin = null;
   protected iConomy economy = null;
   private Accounts accounts;

   public Economy_iConomy6(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_iConomy6.EconomyServerListener(this), plugin);
      this.log.warning("iConomy - If you are using Flatfile storage be aware that versions 6, 7 and 8 have a CRITICAL bug which can wipe ALL iconomy data.");
      this.log.warning("if you're using Votifier, or any other plugin which handles economy data in a threaded manner your server is at risk!");
      this.log.warning("it is highly suggested to use SQL with iCo6 or to use an alternative economy plugin!");
      if (this.economy == null) {
         Plugin ec = plugin.getServer().getPluginManager().getPlugin("iConomy");
         if (ec != null && ec.isEnabled() && ec.getClass().getName().equals("com.iCo6.iConomy")) {
            String version = ec.getDescription().getVersion().split("\\.")[0];
            this.name = this.name + version;
            this.economy = (iConomy)ec;
            this.accounts = new Accounts();
            this.log.info(String.format("[Economy] %s hooked.", this.name));
         }
      }

   }

   public boolean isEnabled() {
      return this.economy == null ? false : this.economy.isEnabled();
   }

   public String getName() {
      return this.name;
   }

   public String format(double amount) {
      return iConomy.format(amount);
   }

   public String currencyNameSingular() {
      return (String)Nodes.Major.getStringList().get(0);
   }

   public String currencyNamePlural() {
      return (String)Nodes.Major.getStringList().get(1);
   }

   public double getBalance(String playerName) {
      return this.accounts.exists(playerName) ? this.accounts.get(playerName).getHoldings().getBalance() : 0.0D;
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
      } else {
         Holdings holdings = this.accounts.get(playerName).getHoldings();
         if (holdings.hasEnough(amount)) {
            holdings.subtract(amount);
            return new EconomyResponse(amount, holdings.getBalance(), EconomyResponse.ResponseType.SUCCESS, (String)null);
         } else {
            return new EconomyResponse(0.0D, holdings.getBalance(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
         }
      }
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds");
      } else {
         Holdings holdings = this.accounts.get(playerName).getHoldings();
         holdings.add(amount);
         return new EconomyResponse(amount, holdings.getBalance(), EconomyResponse.ResponseType.SUCCESS, (String)null);
      }
   }

   public boolean has(String playerName, double amount) {
      return this.getBalance(playerName) >= amount;
   }

   public EconomyResponse createBank(String name, String player) {
      if (this.accounts.exists(name)) {
         return new EconomyResponse(0.0D, this.accounts.get(name).getHoldings().getBalance(), EconomyResponse.ResponseType.FAILURE, "That account already exists.");
      } else {
         boolean created = this.accounts.create(name);
         return created ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "") : new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "There was an error creating the account");
      }
   }

   public EconomyResponse deleteBank(String name) {
      if (this.accounts.exists(name)) {
         this.accounts.remove(new String[]{name});
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.SUCCESS, "");
      } else {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "That bank account does not exist.");
      }
   }

   public EconomyResponse bankHas(String name, double amount) {
      return this.has(name, amount) ? new EconomyResponse(0.0D, amount, EconomyResponse.ResponseType.SUCCESS, "") : new EconomyResponse(0.0D, this.accounts.get(name).getHoldings().getBalance(), EconomyResponse.ResponseType.FAILURE, "The account does not have enough!");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return amount < 0.0D ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds") : this.withdrawPlayer(name, amount);
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return amount < 0.0D ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds") : this.depositPlayer(name, amount);
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "iConomy 6 does not support Bank owners.");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "iConomy 6 does not support Bank members.");
   }

   public EconomyResponse bankBalance(String name) {
      return !this.accounts.exists(name) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "There is no bank account with that name") : new EconomyResponse(0.0D, this.accounts.get(name).getHoldings().getBalance(), EconomyResponse.ResponseType.SUCCESS, (String)null);
   }

   public List<String> getBanks() {
      throw new UnsupportedOperationException("iConomy does not support listing of bank accounts");
   }

   public boolean hasBankSupport() {
      return true;
   }

   public boolean hasAccount(String playerName) {
      return this.accounts.exists(playerName);
   }

   public boolean createPlayerAccount(String playerName) {
      return this.hasAccount(playerName) ? false : this.accounts.create(playerName);
   }

   public int fractionalDigits() {
      return -1;
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
      Economy_iConomy6 economy = null;

      public EconomyServerListener(Economy_iConomy6 economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.economy == null) {
            Plugin ec = event.getPlugin();
            if (ec.getClass().getName().equals("com.iCo6.iConomy")) {
               String version = ec.getDescription().getVersion().split("\\.")[0];
               Economy_iConomy6.this.name = Economy_iConomy6.this.name + version;
               this.economy.economy = (iConomy)ec;
               Economy_iConomy6.this.accounts = new Accounts();
               Economy_iConomy6.this.log.info(String.format("[Economy] %s hooked.", this.economy.name));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.economy != null && event.getPlugin().getDescription().getName().equals("iConomy")) {
            this.economy.economy = null;
            Economy_iConomy6.this.log.info(String.format("[Economy] %s unhooked.", this.economy.name));
         }

      }
   }
}
