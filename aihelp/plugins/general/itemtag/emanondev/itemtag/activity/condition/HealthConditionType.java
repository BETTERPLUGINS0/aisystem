package emanondev.itemtag.activity.condition;

import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HealthConditionType extends DoubleRangeConditionType {
   public HealthConditionType() {
      super("health", (Class)null, true);
   }

   protected double getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
      return player.getHealth();
   }

   protected double getMaxValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
      return player.getMaxHealth();
   }

   @NotNull
   public DoubleRangeConditionType.DoubleRangeCondition read(@NotNull String info, boolean reversed) {
      return new HealthConditionType.HealthRangeCondition(info, reversed);
   }

   private class HealthRangeCondition extends DoubleRangeConditionType.DoubleRangeCondition {
      private final boolean countAbsorption;

      public HealthRangeCondition(String param2, boolean param3) {
         super(info, reversed);
         String[] args = info.split(" ");
         if (args.length > 1) {
            String var5 = args[1].toLowerCase(Locale.ENGLISH);
            byte var6 = -1;
            switch(var5.hashCode()) {
            case 3569038:
               if (var5.equals("true")) {
                  var6 = 0;
               }
               break;
            case 97196323:
               if (var5.equals("false")) {
                  var6 = 1;
               }
            }

            switch(var6) {
            case 0:
               this.countAbsorption = true;
               break;
            case 1:
               this.countAbsorption = false;
               break;
            default:
               throw new IllegalArgumentException();
            }
         } else {
            this.countAbsorption = false;
         }

      }

      protected double getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
         return super.getCurrentValue(player, item, event) + (this.countAbsorption ? player.getAbsorptionAmount() : 0.0D);
      }
   }
}
