package emanondev.itemtag.activity.arguments;

import java.util.Locale;

public class IntRangeArgument extends Argument {
   private final int min;
   private final int max;
   private final boolean inclusive;

   public IntRangeArgument(String info) {
      info = info.split(" ")[0].toLowerCase(Locale.ENGLISH);
      if (info.contains("to")) {
         String[] args = info.split("to");
         int min = Integer.parseInt(args[0]);
         int max = Integer.parseInt(args[1]);
         if (min > max) {
            this.min = max;
            this.max = min;
         } else {
            this.min = min;
            this.max = max;
         }

         this.inclusive = true;
      } else if (info.startsWith("==")) {
         this.min = Integer.parseInt(info.substring(2));
         this.max = this.min;
         this.inclusive = true;
      } else if (info.startsWith("=")) {
         this.min = Integer.parseInt(info.substring(1));
         this.max = this.min;
         this.inclusive = true;
      } else if (info.startsWith(">=")) {
         this.min = Integer.parseInt(info.substring(2));
         this.max = Integer.MAX_VALUE;
         this.inclusive = true;
      } else if (info.startsWith("<=")) {
         this.min = Integer.MIN_VALUE;
         this.max = Integer.parseInt(info.substring(2));
         this.inclusive = true;
      } else if (info.startsWith(">")) {
         this.min = Integer.parseInt(info.substring(1));
         this.max = Integer.MAX_VALUE;
         this.inclusive = false;
      } else if (info.startsWith("<")) {
         this.min = Integer.MIN_VALUE;
         this.max = Integer.parseInt(info.substring(1));
         this.inclusive = false;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public boolean isInside(int amount) {
      if (this.inclusive) {
         return amount >= this.min && amount <= this.max;
      } else {
         return amount > this.min && amount < this.max;
      }
   }

   public boolean isInside(int amount, int max) {
      if (this.inclusive) {
         return amount >= this.min && amount <= max;
      } else {
         return amount > this.min && amount < max;
      }
   }

   public String toString() {
      if (this.min == this.max) {
         return "=" + this.min;
      } else if (this.min == Integer.MIN_VALUE) {
         return "<" + (this.inclusive ? "=" : "") + this.max;
      } else {
         return this.max == Integer.MAX_VALUE ? ">" + (this.inclusive ? "=" : "") + this.min : this.min + "to" + this.max;
      }
   }
}
