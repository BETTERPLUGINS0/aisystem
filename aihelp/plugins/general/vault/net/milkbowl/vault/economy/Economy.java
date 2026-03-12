package net.milkbowl.vault.economy;

import java.util.List;
import org.bukkit.OfflinePlayer;

public interface Economy {
   boolean isEnabled();

   String getName();

   boolean hasBankSupport();

   int fractionalDigits();

   String format(double var1);

   String currencyNamePlural();

   String currencyNameSingular();

   /** @deprecated */
   @Deprecated
   boolean hasAccount(String var1);

   boolean hasAccount(OfflinePlayer var1);

   /** @deprecated */
   @Deprecated
   boolean hasAccount(String var1, String var2);

   boolean hasAccount(OfflinePlayer var1, String var2);

   /** @deprecated */
   @Deprecated
   double getBalance(String var1);

   double getBalance(OfflinePlayer var1);

   /** @deprecated */
   @Deprecated
   double getBalance(String var1, String var2);

   double getBalance(OfflinePlayer var1, String var2);

   /** @deprecated */
   @Deprecated
   boolean has(String var1, double var2);

   boolean has(OfflinePlayer var1, double var2);

   /** @deprecated */
   @Deprecated
   boolean has(String var1, String var2, double var3);

   boolean has(OfflinePlayer var1, String var2, double var3);

   /** @deprecated */
   @Deprecated
   EconomyResponse withdrawPlayer(String var1, double var2);

   EconomyResponse withdrawPlayer(OfflinePlayer var1, double var2);

   /** @deprecated */
   @Deprecated
   EconomyResponse withdrawPlayer(String var1, String var2, double var3);

   EconomyResponse withdrawPlayer(OfflinePlayer var1, String var2, double var3);

   /** @deprecated */
   @Deprecated
   EconomyResponse depositPlayer(String var1, double var2);

   EconomyResponse depositPlayer(OfflinePlayer var1, double var2);

   /** @deprecated */
   @Deprecated
   EconomyResponse depositPlayer(String var1, String var2, double var3);

   EconomyResponse depositPlayer(OfflinePlayer var1, String var2, double var3);

   /** @deprecated */
   @Deprecated
   EconomyResponse createBank(String var1, String var2);

   EconomyResponse createBank(String var1, OfflinePlayer var2);

   EconomyResponse deleteBank(String var1);

   EconomyResponse bankBalance(String var1);

   EconomyResponse bankHas(String var1, double var2);

   EconomyResponse bankWithdraw(String var1, double var2);

   EconomyResponse bankDeposit(String var1, double var2);

   /** @deprecated */
   @Deprecated
   EconomyResponse isBankOwner(String var1, String var2);

   EconomyResponse isBankOwner(String var1, OfflinePlayer var2);

   /** @deprecated */
   @Deprecated
   EconomyResponse isBankMember(String var1, String var2);

   EconomyResponse isBankMember(String var1, OfflinePlayer var2);

   List<String> getBanks();

   /** @deprecated */
   @Deprecated
   boolean createPlayerAccount(String var1);

   boolean createPlayerAccount(OfflinePlayer var1);

   /** @deprecated */
   @Deprecated
   boolean createPlayerAccount(String var1, String var2);

   boolean createPlayerAccount(OfflinePlayer var1, String var2);
}
