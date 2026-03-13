package github.nighter.smartspawner.extras;

import github.nighter.smartspawner.SmartSpawner;
import lombok.Generated;

public class HopperConfig {
   private final boolean hopperEnabled;
   private final int stackPerTransfer;

   public HopperConfig(SmartSpawner plugin) {
      this.hopperEnabled = plugin.getConfig().getBoolean("hopper.enabled", false);
      int amount = plugin.getConfig().getInt("hopper.stack_per_transfer", 5);
      if (amount < 1 || amount > 5) {
         plugin.getLogger().warning("hopper.stack_per_transfer must be 1..5, got " + amount + ". Using 5.");
         amount = 5;
      }

      this.stackPerTransfer = amount;
   }

   @Generated
   public boolean isHopperEnabled() {
      return this.hopperEnabled;
   }

   @Generated
   public int getStackPerTransfer() {
      return this.stackPerTransfer;
   }
}
