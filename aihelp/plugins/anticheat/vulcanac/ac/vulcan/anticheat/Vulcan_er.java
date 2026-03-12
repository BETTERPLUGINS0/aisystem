package ac.vulcan.anticheat;

import java.util.Random;

public class Vulcan_er {
   public static final Random Vulcan_s = new Vulcan_b0();

   public static int Vulcan_R(Object[] var0) {
      return Vulcan_s(new Object[]{Vulcan_s});
   }

   public static int Vulcan_s(Object[] var0) {
      Random var1 = (Random)var0[0];
      return var1.nextInt();
   }

   public static int Vulcan_T(Object[] var0) {
      int var1 = (Integer)var0[0];
      return Vulcan_o(new Object[]{Vulcan_s, new Integer(var1)});
   }

   public static int Vulcan_o(Object[] var0) {
      Random var1 = (Random)var0[0];
      int var2 = (Integer)var0[1];
      return var1.nextInt(var2);
   }

   public static long Vulcan_o(Object[] var0) {
      return Vulcan_B(new Object[]{Vulcan_s});
   }

   public static long Vulcan_B(Object[] var0) {
      Random var1 = (Random)var0[0];
      return var1.nextLong();
   }

   public static boolean Vulcan_P(Object[] var0) {
      return Vulcan_M(new Object[]{Vulcan_s});
   }

   public static boolean Vulcan_M(Object[] var0) {
      Random var1 = (Random)var0[0];
      return var1.nextBoolean();
   }

   public static float Vulcan_c(Object[] var0) {
      return Vulcan_o(new Object[]{Vulcan_s});
   }

   public static float Vulcan_o(Object[] var0) {
      Random var1 = (Random)var0[0];
      return var1.nextFloat();
   }

   public static double Vulcan_x(Object[] var0) {
      return Vulcan_c(new Object[]{Vulcan_s});
   }

   public static double Vulcan_c(Object[] var0) {
      Random var1 = (Random)var0[0];
      return var1.nextDouble();
   }
}
