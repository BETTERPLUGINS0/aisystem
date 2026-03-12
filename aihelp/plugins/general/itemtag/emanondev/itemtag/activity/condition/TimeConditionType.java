package emanondev.itemtag.activity.condition;

import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimeConditionType extends DoubleRangeConditionType {
   public TimeConditionType() {
      super("time", (Class)null, false);
   }

   protected double getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
      return (double)player.getWorld().getTime();
   }

   @NotNull
   public DoubleRangeConditionType.DoubleRangeCondition read(@NotNull String info, boolean reversed) {
      return new TimeConditionType.HealthRangeCondition(info, reversed);
   }

   private class HealthRangeCondition extends DoubleRangeConditionType.DoubleRangeCondition {
      private final boolean playerRelative;

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
               this.playerRelative = true;
               break;
            case 1:
               this.playerRelative = false;
               break;
            default:
               throw new IllegalArgumentException();
            }
         } else {
            this.playerRelative = false;
         }

      }

      protected double getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
         if (!this.playerRelative) {
            return super.getCurrentValue(player, item, event) % 24000.0D;
         } else {
            return !player.isPlayerTimeRelative() ? (double)(player.getPlayerTimeOffset() % 24000L) : ((double)player.getPlayerTimeOffset() + super.getCurrentValue(player, item, event)) % 24000.0D;
         }
      }
   }
}
