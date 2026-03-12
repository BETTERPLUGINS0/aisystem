package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class CooldownData {
   private int ticksRemaining;
   private final int transaction;

   @Contract(
      mutates = "this"
   )
   public void tick() {
      --this.ticksRemaining;
   }

   @Generated
   public CooldownData(int ticksRemaining, int transaction) {
      this.ticksRemaining = ticksRemaining;
      this.transaction = transaction;
   }

   @Generated
   public int getTicksRemaining() {
      return this.ticksRemaining;
   }

   @Generated
   public int getTransaction() {
      return this.transaction;
   }

   @Generated
   public void setTicksRemaining(int ticksRemaining) {
      this.ticksRemaining = ticksRemaining;
   }
}
