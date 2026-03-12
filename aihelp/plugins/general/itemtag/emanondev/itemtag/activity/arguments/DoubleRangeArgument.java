package emanondev.itemtag.activity.arguments;

import java.util.Locale;

public class DoubleRangeArgument extends Argument {
   private final double min;
   private final double max;
   private final boolean inclusive;
   private final boolean percent;

   public DoubleRangeArgument(String info, boolean allowPercent) {
      info = info.split(" ")[0].toLowerCase(Locale.ENGLISH);
      if (info.endsWith("%")) {
         if (!allowPercent) {
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

   public boolean isPercent() {
      return this.percent;
   }

   public boolean isInside(double amount) {
      if (this.percent) {
         (new IllegalArgumentException("Using percent value as absolute")).printStackTrace();
      }

      if (this.inclusive) {
         return amount >= this.min && amount <= this.max;
      } else {
         return amount > this.min && amount < this.max;
      }
   }

   public boolean isInside(double amount, double max) {
      if (this.percent) {
         amount = amount * 100.0D / max;
      }

      if (this.inclusive) {
         return amount >= this.min && amount <= max;
      } else {
         return amount > this.min && amount < max;
      }
   }

   public String toString() {
      if (this.min == this.max) {
         return "=" + this.min + (this.percent ? "" : "%");
      } else if (this.min == Double.MIN_VALUE) {
         return "<" + (this.inclusive ? "=" : "") + this.max + (this.percent ? "" : "%");
      } else {
         return this.max == Double.MAX_VALUE ? ">" + (this.inclusive ? "=" : "") + this.min + (this.percent ? "" : "%") : this.min + "to" + this.max;
      }
   }
}
