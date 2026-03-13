package tntrun.arena.handlers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class ArenaEconomy {
   private final TNTRun plugin;
   private Arena arena;

   public ArenaEconomy(TNTRun plugin, Arena arena) {
      this.plugin = plugin;
      this.arena = arena;
   }

   public boolean hasMoney(double moneyneed, Player player) {
      return this.hasMoney(moneyneed, player, false);
   }

   public boolean hasMoney(double moneyneed, Player player, boolean checkonly) {
      Economy econ = this.plugin.getVaultHandler().getEconomy();
      if (econ == null) {
         return false;
      } else {
         OfflinePlayer offplayer = player.getPlayer();
         double pmoney = econ.getBalance(offplayer);
         if (pmoney >= moneyneed) {
            if (!checkonly) {
               econ.withdrawPlayer(offplayer, moneyneed);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public double getPlayerBalance(Player player) {
      Economy econ = this.plugin.getVaultHandler().getEconomy();
      if (econ == null) {
         return 0.0D;
      } else {
         OfflinePlayer offplayer = player.getPlayer();
         return econ.getBalance(offplayer);
      }
   }

   private boolean hasItemCurrency(Player player, Material currency, int fee, boolean checkonly) {
      if (!player.getInventory().contains(currency, fee)) {
         return false;
      } else {
         if (!checkonly) {
            player.getInventory().removeItem(new ItemStack[]{new ItemStack(currency, fee)});
         }

         return true;
      }
   }

   public boolean hasFunds(Player player, double fee, boolean checkonly) {
      return this.arena.getStructureManager().isCurrencyEnabled() ? this.hasItemCurrency(player, this.arena.getStructureManager().getCurrency(), (int)fee, checkonly) : this.hasMoney(fee, player, checkonly);
   }
}
