package ac.vulcan.anticheat;

import java.io.PrintStream;
import java.io.PrintWriter;

public class Vulcan_ig extends Error implements Vulcan_iX {
   private static final long serialVersionUID = 1L;
   protected Vulcan_eK Vulcan_Z = new Vulcan_eK(this);
   private Throwable Vulcan_I = null;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3991335506316084261L, -1034756731329080779L, (Object)null).a(60327803595818L);

   public Vulcan_ig() {
   }

   public Vulcan_ig(String var1) {
      super(var1);
   }

   public Vulcan_ig(Throwable var1) {
      this.Vulcan_I = var1;
   }

   public Vulcan_ig(String var1, Throwable var2) {
      super(var1);
      this.Vulcan_I = var2;
   }

   public Throwable getCause() {
      return this.Vulcan_I;
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

      return this.Vulcan_Z.Vulcan_e(new Object[]{new Long(var5), new Integer(var2)});
   }

   public String[] Vulcan_K(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 25628550657000L;
      return this.Vulcan_Z.Vulcan_A(new Object[]{new Long(var4)});
   }

   public Throwable Vulcan_w(Object[] var1) {
      int var4 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      long var5 = var2 ^ 112738229874061L;
      return this.Vulcan_Z.Vulcan_m(new Object[]{new Long(var5), new Integer(var4)});
   }

   public int Vulcan_x(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 134422025844835L;
      return this.Vulcan_Z.Vulcan_N(new Object[]{new Long(var4)});
   }

   public Throwable[] Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 39448265570914L;
      return this.Vulcan_Z.Vulcan_W(new Object[]{new Long(var4)});
   }

   public int Vulcan_z(Object[] var1) {
      long var3 = (Long)var1[0];
      Class var2 = (Class)var1[1];
      long var5 = var3 ^ 63460973614450L;
      return this.Vulcan_Z.Vulcan_a(new Object[]{var2, new Integer(0), new Long(var5)});
   }

   public int Vulcan_T(Object[] var1) {
      Class var5 = (Class)var1[0];
      long var3 = (Long)var1[1];
      int var2 = (Integer)var1[2];
      long var6 = var3 ^ 6363077939541L;
      return this.Vulcan_Z.Vulcan_a(new Object[]{var5, new Integer(var2), new Long(var6)});
   }

   public void printStackTrace() {
      long var1 = a ^ 102327154654315L;
      long var3 = var1 ^ 57776133473791L;
      this.Vulcan_Z.Vulcan_k(new Object[]{new Long(var3)});
   }

   public void printStackTrace(PrintStream var1) {
      long var2 = a ^ 83338233287933L;
      long var4 = var2 ^ 67330849304707L;
      this.Vulcan_Z.Vulcan_b(new Object[]{var1, new Long(var4)});
   }

   public void printStackTrace(PrintWriter var1) {
      long var2 = a ^ 63079153542718L;
      long var4 = var2 ^ 45147301218826L;
      this.Vulcan_Z.Vulcan_s(new Object[]{var1, new Long(var4)});
   }

   public final void Vulcan_T(Object[] var1) {
      PrintWriter var2 = (PrintWriter)var1[0];
      super.printStackTrace(var2);
   }

   private static Vulcan_XW a(Vulcan_XW var0) {
      return var0;
   }
}
