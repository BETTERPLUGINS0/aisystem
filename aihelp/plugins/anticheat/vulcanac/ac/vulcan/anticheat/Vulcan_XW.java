package ac.vulcan.anticheat;

import java.io.PrintStream;
import java.io.PrintWriter;

public class Vulcan_XW extends RuntimeException implements Vulcan_iX {
   private static final long serialVersionUID = 1L;
   protected Vulcan_eK Vulcan_x = new Vulcan_eK(this);
   private Throwable Vulcan_J = null;
   private static String Vulcan_v;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-2937568181775501396L, 4161115204515681198L, (Object)null).a(112463231681644L);

   public Vulcan_XW() {
   }

   public Vulcan_XW(String var1) {
      super(var1);
   }

   public Vulcan_XW(Throwable var1) {
      this.Vulcan_J = var1;
   }

   public Vulcan_XW(String var1, Throwable var2) {
      super(var1);
      this.Vulcan_J = var2;
   }

   public Throwable getCause() {
      return this.Vulcan_J;
   }

   public String getMessage() {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_o(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      long var5 = var3 ^ 44954694477396L;

      try {
         if (var2 == 0) {
            return super.getMessage();
         }
      } catch (Vulcan_XW var7) {
         throw a(var7);
      }

      return this.Vulcan_x.Vulcan_e(new Object[]{new Long(var5), new Integer(var2)});
   }

   public String[] Vulcan_K(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 25628550657000L;
      return this.Vulcan_x.Vulcan_A(new Object[]{new Long(var4)});
   }

   public Throwable Vulcan_w(Object[] var1) {
      int var4 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      long var5 = var2 ^ 112738229874061L;
      return this.Vulcan_x.Vulcan_m(new Object[]{new Long(var5), new Integer(var4)});
   }

   public int Vulcan_x(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 134422025844835L;
      return this.Vulcan_x.Vulcan_N(new Object[]{new Long(var4)});
   }

   public Throwable[] Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 39448265570914L;
      return this.Vulcan_x.Vulcan_W(new Object[]{new Long(var4)});
   }

   public int Vulcan_z(Object[] var1) {
      long var2 = (Long)var1[0];
      Class var4 = (Class)var1[1];
      long var5 = var2 ^ 63460973614450L;
      return this.Vulcan_x.Vulcan_a(new Object[]{var4, new Integer(0), new Long(var5)});
   }

   public int Vulcan_T(Object[] var1) {
      Class var3 = (Class)var1[0];
      long var4 = (Long)var1[1];
      int var2 = (Integer)var1[2];
      long var6 = var4 ^ 6363077939541L;
      return this.Vulcan_x.Vulcan_a(new Object[]{var3, new Integer(var2), new Long(var6)});
   }

   public void printStackTrace() {
      long var1 = a ^ 41427195417777L;
      long var3 = var1 ^ 122718160815657L;
      this.Vulcan_x.Vulcan_k(new Object[]{new Long(var3)});
   }

   public void printStackTrace(PrintStream var1) {
      long var2 = a ^ 66642577710635L;
      long var4 = var2 ^ 116301552963929L;
      this.Vulcan_x.Vulcan_b(new Object[]{var1, new Long(var4)});
   }

   public void printStackTrace(PrintWriter var1) {
      long var2 = a ^ 134169749823011L;
      long var4 = var2 ^ 81339624511771L;
      this.Vulcan_x.Vulcan_s(new Object[]{var1, new Long(var4)});
   }

   public final void Vulcan_T(Object[] var1) {
      PrintWriter var2 = (PrintWriter)var1[0];
      super.printStackTrace(var2);
   }

   public static void Vulcan_U(String var0) {
      Vulcan_v = var0;
   }

   public static String Vulcan_x() {
      return Vulcan_v;
   }

   static {
      if (Vulcan_x() != null) {
         Vulcan_U("DFMGfb");
      }

   }

   private static Vulcan_XW a(Vulcan_XW var0) {
      return var0;
   }
}
