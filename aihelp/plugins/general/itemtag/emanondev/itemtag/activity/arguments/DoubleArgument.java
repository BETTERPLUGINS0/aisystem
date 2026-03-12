package emanondev.itemtag.activity.arguments;

public class DoubleArgument extends Argument {
   private final double min;
   private final double max;
   private double value;

   public DoubleArgument(double value) {
      this(value, Double.MIN_VALUE, Double.MAX_VALUE);
   }

   public DoubleArgument(double value, double min, double max) {
      if (min > max) {
         throw new IllegalArgumentException();
      } else {
         this.min = min;
         this.max = max;
         this.value = this.bound(value);
      }
   }

   public DoubleArgument(String info) {
      this(Double.parseDouble(info));
   }

   public DoubleArgument(String info, double min, double max) {
      this(Double.parseDouble(info), min, max);
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double value) {
      this.value = this.bound(value);
   }

   private double bound(double value) {
      return Math.max(this.min, Math.min(this.max, value));
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
