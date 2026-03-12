package ac.vulcan.anticheat;

import java.io.PrintStream;
import java.io.PrintWriter;

public class Vulcan_iw extends Exception implements Vulcan_iX {
   private static final long serialVersionUID = 1L;
   protected Vulcan_eK Vulcan_W = new Vulcan_eK(this);
   private Throwable Vulcan_j = null;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(5802285311120184005L, -8994359215826492310L, (Object)null).a(228894476578448L);

   public Vulcan_iw() {
   }

   public Vulcan_iw(String var1) {
      super(var1);
   }

   public Vulcan_iw(Throwable var1) {
      this.Vulcan_j = var1;
   }

   public Vulcan_iw(String var1, Throwable var2) {
      super(var1);
      this.Vulcan_j = var2;
   }

   public Throwable getCause() {
      return this.Vulcan_j;
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

      return this.Vulcan_W.Vulcan_e(new Object[]{new Long(var5), new Integer(var2)});
   }

   public String[] Vulcan_K(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 25628550657000L;
      return this.Vulcan_W.Vulcan_A(new Object[]{new Long(var4)});
   }

   public Throwable Vulcan_w(Object[] var1) {
      int var4 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      long var5 = var2 ^ 112738229874061L;
      return this.Vulcan_W.Vulcan_m(new Object[]{new Long(var5), new Integer(var4)});
   }

   public int Vulcan_x(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 134422025844835L;
      return this.Vulcan_W.Vulcan_N(new Object[]{new Long(var4)});
   }

   public Throwable[] Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 39448265570914L;
      return this.Vulcan_W.Vulcan_W(new Object[]{new Long(var4)});
   }

   public int Vulcan_z(Object[] var1) {
      long var3 = (Long)var1[0];
      Class var2 = (Class)var1[1];
      long var5 = var3 ^ 63460973614450L;
      return this.Vulcan_W.Vulcan_a(new Object[]{var2, new Integer(0), new Long(var5)});
   }

   public int Vulcan_T(Object[] var1) {
      Class var5 = (Class)var1[0];
      long var2 = (Long)var1[1];
      int var4 = (Integer)var1[2];
      long var6 = var2 ^ 6363077939541L;
      return this.Vulcan_W.Vulcan_a(new Object[]{var5, new Integer(var4), new Long(var6)});
   }

   public void printStackTrace() {
      long var1 = a ^ 19349886638829L;
      long var3 = var1 ^ 107960973233311L;
      this.Vulcan_W.Vulcan_k(new Object[]{new Long(var3)});
   }

   public void printStackTrace(PrintStream var1) {
      long var2 = a ^ 127237450894014L;
      long var4 = var2 ^ 34611291144486L;
      this.Vulcan_W.Vulcan_b(new Object[]{var1, new Long(var4)});
   }

   public void printStackTrace(PrintWriter var1) {
      long var2 = a ^ 9463782229975L;
      long var4 = var2 ^ 3295632835589L;
      this.Vulcan_W.Vulcan_s(new Object[]{var1, new Long(var4)});
   }

   public final void Vulcan_T(Object[] var1) {
      PrintWriter var2 = (PrintWriter)var1[0];
      super.printStackTrace(var2);
   }

   private static Vulcan_XW a(Vulcan_XW var0) {
      return var0;
   }
}
