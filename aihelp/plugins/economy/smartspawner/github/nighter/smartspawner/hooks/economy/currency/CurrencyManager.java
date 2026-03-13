package github.nighter.smartspawner.hooks.economy.currency;

import github.nighter.smartspawner.SmartSpawner;
import java.util.logging.Level;
import lombok.Generated;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public class CurrencyManager {
   private final SmartSpawner plugin;
   private boolean currencyAvailable = false;
   private String activeCurrencyProvider = "None";
   private Economy vaultEconomy;
   private Currency coinsEngineCurrency;
   private String configuredCurrencyType;
   private String configuredCoinsEngineCurrency;

   public CurrencyManager(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   public void initialize() {
      this.loadConfiguration();
      this.setupCurrency();
   }

   private void loadConfiguration() {
      this.configuredCurrencyType = this.plugin.getConfig().getString("custom_economy.currency", "VAULT");
      this.configuredCoinsEngineCurrency = this.plugin.getConfig().getString("custom_economy.coinsengine_currency", "coins");
   }

   private void setupCurrency() {
      this.currencyAvailable = false;
      this.activeCurrencyProvider = "None";
      if (this.configuredCurrencyType.equalsIgnoreCase("VAULT")) {
         this.currencyAvailable = this.setupVaultEconomy();
      } else if (this.configuredCurrencyType.equalsIgnoreCase("COINSENGINE")) {
         this.currencyAvailable = this.setupCoinsEngineEconomy();
      } else {
         this.plugin.getLogger().warning("Unsupported currency type: " + this.configuredCurrencyType + ". Currently only VAULT is supported.");
         this.plugin.getLogger().warning("Economy features will be disabled.");
      }

   }

   private boolean setupVaultEconomy() {
      if (this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
         this.plugin.getLogger().warning("Vault not found! Selling items from spawner will be disabled.");
         return false;
      } else {
         try {
            RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
               this.plugin.getLogger().warning("No economy provider found for Vault! Selling items from spawner will be disabled.");
               return false;
            } else {
               this.vaultEconomy = (Economy)rsp.getProvider();
               if (this.vaultEconomy == null) {
                  this.plugin.getLogger().warning("Failed to get economy provider from Vault! Selling items from spawner will be disabled.");
                  return false;
               } else {
                  this.activeCurrencyProvider = "Vault (" + this.vaultEconomy.getName() + ")";
                  this.plugin.getLogger().info("Successfully connected to Vault & Economy provider: " + this.vaultEconomy.getName());
                  return true;
               }
            }
         } catch (Exception var2) {
            this.plugin.getLogger().log(Level.SEVERE, "Error setting up Vault economy integration", var2);
            return false;
         }
      }
   }

   private boolean setupCoinsEngineEconomy() {
      if (this.plugin.getServer().getPluginManager().getPlugin("CoinsEngine") == null) {
         this.plugin.getLogger().warning("CoinsEngine not found! Selling items from spawner will be disabled.");
         return false;
      } else {
         try {
            this.coinsEngineCurrency = CoinsEngineAPI.getCurrency(this.configuredCoinsEngineCurrency);
            if (this.coinsEngineCurrency == null) {
               this.plugin.getLogger().warning("Could not find CoinsEngine currency '" + this.configuredCoinsEngineCurrency + "'. Selling items from spawner will be disabled.");
               return false;
            } else {
               this.activeCurrencyProvider = "CoinsEngine (" + this.coinsEngineCurrency.getName() + ")";
               this.plugin.getLogger().info("Successfully connected to CoinsEngine with currency: " + this.coinsEngineCurrency.getName());
               return true;
            }
         } catch (Exception var2) {
            this.plugin.getLogger().log(Level.SEVERE, "Error setting up CoinsEngine economy integration", var2);
            return false;
         }
      }
   }

   public boolean deposit(double amount, OfflinePlayer player) {
      if (!this.currencyAvailable) {
         this.plugin.getLogger().warning("Currency not available for deposit operation.");
         return false;
      } else if (this.configuredCurrencyType.equalsIgnoreCase("VAULT")) {
         if (this.vaultEconomy == null) {
            this.plugin.getLogger().warning("Vault economy is not initialized.");
            return false;
         } else {
            return this.vaultEconomy.depositPlayer(player, amount).transactionSuccess();
         }
      } else if (this.configuredCurrencyType.equalsIgnoreCase("COINSENGINE")) {
         if (this.coinsEngineCurrency == null) {
            this.plugin.getLogger().warning("CoinsEngine currency is not initialized.");
            return false;
         } else {
            CoinsEngineAPI.addBalance(player.getUniqueId(), this.coinsEngineCurrency, amount);
            return true;
         }
      } else {
         this.plugin.getLogger().warning("Unsupported currency type during deposit: " + this.configuredCurrencyType);
         return false;
      }
   }

   public void withdraw(double amount, OfflinePlayer player) {
      if (!this.currencyAvailable) {
         this.plugin.getLogger().warning("Currency not available for withdraw operation.");
      } else if (this.configuredCurrencyType.equalsIgnoreCase("VAULT")) {
         if (this.vaultEconomy == null) {
            this.plugin.getLogger().warning("Vault economy is not initialized.");
         } else {
            this.vaultEconomy.withdrawPlayer(player, amount).transactionSuccess();
         }
      } else if (this.configuredCurrencyType.equalsIgnoreCase("COINSENGINE")) {
         if (this.coinsEngineCurrency == null) {
            this.plugin.getLogger().warning("CoinsEngine currency is not initialized.");
         } else {
            CoinsEngineAPI.removeBalance(player.getUniqueId(), this.coinsEngineCurrency, amount);
         }
      } else {
         this.plugin.getLogger().warning("Unsupported currency type during withdraw: " + this.configuredCurrencyType);
      }
   }

   public void reload() {
      this.cleanup();
      this.loadConfiguration();
      this.setupCurrency();
   }

   public void cleanup() {
      this.vaultEconomy = null;
      this.coinsEngineCurrency = null;
      this.currencyAvailable = false;
      this.activeCurrencyProvider = "None";
   }

   @Generated
   public boolean isCurrencyAvailable() {
      return this.currencyAvailable;
   }

   @Generated
   public String getActiveCurrencyProvider() {
      return this.activeCurrencyProvider;
   }

   @Generated
   public String getConfiguredCurrencyType() {
      return this.configuredCurrencyType;
   }

   @Generated
   public String getConfiguredCoinsEngineCurrency() {
      return this.configuredCoinsEngineCurrency;
   }
}
