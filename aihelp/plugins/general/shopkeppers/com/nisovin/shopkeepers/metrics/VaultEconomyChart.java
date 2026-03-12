package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.libs.bstats.Metrics;
import com.nisovin.shopkeepers.util.java.StringUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomyChart extends Metrics.SimplePie {
   private static final String ECONOMY_SERVICE_CLASS_NAME = "net.milkbowl.vault.economy.Economy";

   public VaultEconomyChart() {
      super("vault_economy", () -> {
         Class economyClass = null;

         try {
            economyClass = Class.forName("net.milkbowl.vault.economy.Economy");
         } catch (ClassNotFoundException var4) {
         }

         String economyName = null;
         if (economyClass != null) {
            RegisteredServiceProvider<Economy> registration = Bukkit.getServicesManager().getRegistration(Economy.class);
            Economy economy = registration != null ? (Economy)registration.getProvider() : null;
            if (economy != null) {
               economyName = economy.getName();
            }
         }

         return StringUtils.isEmpty(economyName) ? "None" : (String)Unsafe.assertNonNull(economyName);
      });
   }
}
