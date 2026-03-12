package emanondev.itemtag.activity.arguments;

public class IntArgument extends Argument {
   private final int min;
   private final int max;
   private int value;

   public IntArgument(int value) {
      this(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   public IntArgument(int value, int min, int max) {
      if (min > max) {
         throw new IllegalArgumentException();
      } else {
         this.min = min;
         this.max = max;
         this.value = this.bound(value);
      }
   }

   public IntArgument(String info) {
      this(Integer.parseInt(info));
   }

   public IntArgument(String info, int min, int max) {
      this(Integer.parseInt(info), min, max);
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = this.bound(value);
   }

   private int bound(int value) {
      return Math.max(this.min, Math.min(this.max, value));
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
