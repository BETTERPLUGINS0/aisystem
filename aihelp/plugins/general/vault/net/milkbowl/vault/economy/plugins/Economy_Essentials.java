package net.milkbowl.vault.economy.plugins;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
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

public class Economy_Essentials extends AbstractEconomy {
   private final String name = "Essentials Economy";
   private final Logger log;
   private Plugin plugin = null;
   private Essentials ess = null;

   public Economy_Essentials(Plugin plugin) {
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Economy_Essentials.EconomyServerListener(this), plugin);
      if (this.ess == null) {
         Plugin essentials = plugin.getServer().getPluginManager().getPlugin("Essentials");
         if (essentials != null && essentials.isEnabled()) {
            this.ess = (Essentials)essentials;
            this.log.info(String.format("[Economy] %s hooked.", "Essentials Economy"));
         }
      }

   }

   public boolean isEnabled() {
      return this.ess == null ? false : this.ess.isEnabled();
   }

   public String getName() {
      return "Essentials Economy";
   }

   public double getBalance(String playerName) {
      double balance;
      try {
         balance = Economy.getMoney(playerName);
      } catch (UserDoesNotExistException var5) {
         this.createPlayerAccount(playerName);
         balance = 0.0D;
      }

      return balance;
   }

   public boolean createPlayerAccount(String playerName) {
      return this.hasAccount(playerName) ? false : Economy.createNPC(playerName);
   }

   public EconomyResponse withdrawPlayer(String playerName, double amount) {
      if (playerName == null) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Player name can not be null.");
      } else if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
      } else {
         String errorMessage = null;

         double balance;
         EconomyResponse.ResponseType type;
         try {
            Economy.subtract(playerName, amount);
            balance = Economy.getMoney(playerName);
            type = EconomyResponse.ResponseType.SUCCESS;
         } catch (UserDoesNotExistException var11) {
            if (this.createPlayerAccount(playerName)) {
               return this.withdrawPlayer(playerName, amount);
            }

            amount = 0.0D;
            balance = 0.0D;
            type = EconomyResponse.ResponseType.FAILURE;
            errorMessage = "User does not exist";
         } catch (NoLoanPermittedException var12) {
            try {
               balance = Economy.getMoney(playerName);
               amount = 0.0D;
               type = EconomyResponse.ResponseType.FAILURE;
               errorMessage = "Loan was not permitted";
            } catch (UserDoesNotExistException var10) {
               amount = 0.0D;
               balance = 0.0D;
               type = EconomyResponse.ResponseType.FAILURE;
               errorMessage = "User does not exist";
            }
         }

         return new EconomyResponse(amount, balance, type, errorMessage);
      }
   }

   public EconomyResponse tryDepositPlayer(String playerName, double amount, int tries) {
      if (playerName == null) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Player name can not be null.");
      } else if (amount < 0.0D) {
         return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds");
      } else if (tries <= 0) {
         return new EconomyResponse(amount, 0.0D, EconomyResponse.ResponseType.FAILURE, "Failed to deposit amount.");
      } else {
         String errorMessage = null;

         double balance;
         EconomyResponse.ResponseType type;
         try {
            Economy.add(playerName, amount);
            balance = Economy.getMoney(playerName);
            type = EconomyResponse.ResponseType.SUCCESS;
         } catch (UserDoesNotExistException var12) {
            if (this.createPlayerAccount(playerName)) {
               return this.tryDepositPlayer(playerName, amount, tries--);
            }

            amount = 0.0D;
            balance = 0.0D;
            type = EconomyResponse.ResponseType.FAILURE;
            errorMessage = "User does not exist";
         } catch (NoLoanPermittedException var13) {
            try {
               balance = Economy.getMoney(playerName);
               amount = 0.0D;
               type = EconomyResponse.ResponseType.FAILURE;
               errorMessage = "Loan was not permitted";
            } catch (UserDoesNotExistException var11) {
               balance = 0.0D;
               amount = 0.0D;
               type = EconomyResponse.ResponseType.FAILURE;
               errorMessage = "Loan was not permitted";
            }
         }

         return new EconomyResponse(amount, balance, type, errorMessage);
      }
   }

   public EconomyResponse depositPlayer(String playerName, double amount) {
      return this.tryDepositPlayer(playerName, amount, 2);
   }

   public String format(double amount) {
      return Economy.format(amount);
   }

   public String currencyNameSingular() {
      return "";
   }

   public String currencyNamePlural() {
      return "";
   }

   public boolean has(String playerName, double amount) {
      try {
         return Economy.hasEnough(playerName, amount);
      } catch (UserDoesNotExistException var5) {
         return false;
      }
   }

   public EconomyResponse createBank(String name, String player) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse deleteBank(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse bankHas(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse bankWithdraw(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse bankDeposit(String name, double amount) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse isBankOwner(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse isBankMember(String name, String playerName) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public EconomyResponse bankBalance(String name) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Essentials Eco does not support bank accounts!");
   }

   public List<String> getBanks() {
      return new ArrayList();
   }

   public boolean hasBankSupport() {
      return false;
   }

   public boolean hasAccount(String playerName) {
      return Economy.playerExists(playerName);
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
      Economy_Essentials economy = null;

      public EconomyServerListener(Economy_Essentials economy) {
         this.economy = economy;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.economy.ess == null) {
            Plugin essentials = event.getPlugin();
            if (essentials.getDescription().getName().equals("Essentials")) {
               this.economy.ess = (Essentials)essentials;
               Economy_Essentials.this.log.info(String.format("[Economy] %s hooked.", "Essentials Economy"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.economy.ess != null && event.getPlugin().getDescription().getName().equals("Essentials")) {
            this.economy.ess = null;
            Economy_Essentials.this.log.info(String.format("[Economy] %s unhooked.", "Essentials Economy"));
         }

      }
   }
}
