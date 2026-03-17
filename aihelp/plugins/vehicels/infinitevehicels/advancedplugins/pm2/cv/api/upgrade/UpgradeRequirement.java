package advancedplugins.pm2.cv.api.upgrade;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import java.util.Objects;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public abstract class UpgradeRequirement {
   public abstract int getAmount();

   public abstract void setAmount(int var1);

   public abstract String takeRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3);

   public abstract String testRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3);

   public abstract String getType();

   public static UpgradeRequirement load(String var0, int var1) {
      byte var3 = -1;
      switch(var0.hashCode()) {
      case 100893:
         if (var0.equals("exp")) {
            var3 = 1;
         }
         break;
      case 102865796:
         if (var0.equals("level")) {
            var3 = 2;
         }
         break;
      case 315947365:
         if (var0.equals("vault-money")) {
            var3 = 0;
         }
      }

      Object var10000;
      switch(var3) {
      case 0:
         var10000 = new UpgradeRequirement.VaultRequirement(var1);
         break;
      case 1:
         var10000 = new UpgradeRequirement.ExpRequirement(var1);
         break;
      case 2:
         var10000 = new UpgradeRequirement.LevelRequirement(var1);
         break;
      default:
         var10000 = null;
      }

      return (UpgradeRequirement)var10000;
   }

   public static class VaultRequirement extends UpgradeRequirement {
      private int amount;

      public String takeRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3) {
         if (this.testRequirement(var1, var2, var3) != null) {
            return this.testRequirement(var1, var2, var3);
         } else {
            RegisteredServiceProvider var4 = InfiniteVehicles.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
            ((Economy)((RegisteredServiceProvider)Objects.requireNonNull(var4)).getProvider()).withdrawPlayer(var3, (double)this.amount);
            return null;
         }
      }

      public String testRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3) {
         if (InfiniteVehicles.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return "&cVault is not installed!";
         } else {
            RegisteredServiceProvider var4 = InfiniteVehicles.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
            if (var4 == null) {
               return "&cUnable to find a valid economy plugin!";
            } else {
               Economy var5 = (Economy)var4.getProvider();
               return var5.getBalance(var3) < (double)this.amount ? "&cYou need " + this.amount + " money to unlock this upgrade!" : null;
            }
         }
      }

      public String getType() {
         return "vault-money";
      }

      public int getAmount() {
         return this.amount;
      }

      public void setAmount(int var1) {
         this.amount = var1;
      }

      public VaultRequirement(int var1) {
         this.amount = var1;
      }
   }

   public static class ExpRequirement extends UpgradeRequirement {
      private int amount;

      public String takeRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3) {
         if (this.testRequirement(var1, var2, var3) != null) {
            return this.testRequirement(var1, var2, var3);
         } else {
            var3.setTotalExperience(var3.getTotalExperience() - this.amount);
            return null;
         }
      }

      public String testRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3) {
         return var3.getTotalExperience() - this.amount < 0 ? "&cYou need " + this.amount + " exp to unlock this upgrade!" : null;
      }

      public String getType() {
         return "exp";
      }

      public int getAmount() {
         return this.amount;
      }

      public void setAmount(int var1) {
         this.amount = var1;
      }

      public ExpRequirement(int var1) {
         this.amount = var1;
      }
   }

   public static class LevelRequirement extends UpgradeRequirement {
      private int amount;

      public String takeRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3) {
         if (this.testRequirement(var1, var2, var3) != null) {
            return this.testRequirement(var1, var2, var3);
         } else {
            var3.setLevel(var3.getLevel() - this.amount);
            return null;
         }
      }

      public String testRequirement(@NotNull Upgrade var1, @NotNull UpgradeTier var2, @NotNull Player var3) {
         return var3.getLevel() - this.amount < 0 ? "&cYou need " + this.amount + " level to unlock this upgrade!" : null;
      }

      public String getType() {
         return "level";
      }

      public int getAmount() {
         return this.amount;
      }

      public void setAmount(int var1) {
         this.amount = var1;
      }

      public LevelRequirement(int var1) {
         this.amount = var1;
      }
   }
}
