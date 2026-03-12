package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DoubleRangeConditionType extends ConditionType {
   private final boolean allowPercent;

   public DoubleRangeConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz, boolean allowPercent) {
      super(id, clazz);
      this.allowPercent = allowPercent;
   }

   protected double getMaxValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
      throw new UnsupportedOperationException();
   }

   protected abstract double getCurrentValue(@NotNull Player var1, @NotNull ItemStack var2, @Nullable Event var3);

   @NotNull
   public DoubleRangeConditionType.DoubleRangeCondition read(@NotNull String info, boolean reversed) {
      return new DoubleRangeConditionType.DoubleRangeCondition(info, reversed);
   }

   public class DoubleRangeCondition extends ConditionType.Condition {
      private final double min;
      private final double max;
      private final boolean inclusive;
      private final boolean percent;

      public DoubleRangeCondition(@NotNull String param2, boolean param3) {
         super(info, reversed);
         info = info.split(" ")[0].toLowerCase(Locale.ENGLISH);
         if (info.endsWith("%")) {
            if (!DoubleRangeConditionType.this.allowPercent) {
               throw new IllegalArgumentException();
            }

            this.percent = true;
            info = info.substring(0, info.length() - 1);
         } else {
            this.percent = false;
         }

         if (info.contains("to")) {
            String[] args = info.split("to");
            double min = Double.parseDouble(args[0]);
            double max = Double.parseDouble(args[1]);
            if (min > max) {
               this.min = max;
               this.max = min;
            } else {
               this.min = min;
               this.max = max;
            }

            this.inclusive = true;
         } else if (info.startsWith("==")) {
            this.min = Double.parseDouble(info.substring(2));
            this.max = this.min;
            this.inclusive = true;
         } else if (info.startsWith("=")) {
            this.min = Double.parseDouble(info.substring(1));
            this.max = this.min;
            this.inclusive = true;
         } else if (info.startsWith(">=")) {
            this.min = Double.parseDouble(info.substring(2));
            this.max = Double.MAX_VALUE;
            this.inclusive = true;
         } else if (info.startsWith("<=")) {
            this.min = Double.MIN_VALUE;
            this.max = Double.parseDouble(info.substring(2));
            this.inclusive = true;
         } else if (info.startsWith(">")) {
            this.min = Double.parseDouble(info.substring(1));
            this.max = Double.MAX_VALUE;
            this.inclusive = false;
         } else if (info.startsWith("<")) {
            this.min = Double.MIN_VALUE;
            this.max = Double.parseDouble(info.substring(1));
            this.inclusive = false;
         } else {
            throw new IllegalArgumentException();
         }
      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
         double amount = this.getCurrentValue(player, item, event);
         if (this.percent) {
            amount = amount * 100.0D / this.getMaxValue(player, item, event);
         }

         if (this.inclusive) {
            return amount >= this.min && amount <= this.max;
         } else {
            return amount > this.min && amount < this.max;
         }
      }

      protected double getMaxValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
         return DoubleRangeConditionType.this.getMaxValue(player, item, event);
      }

      protected double getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
         return DoubleRangeConditionType.this.getCurrentValue(player, item, event);
      }
   }
}
