package ac.vulcan.anticheat;

import java.io.PrintStream;
import java.io.PrintWriter;

public class Vulcan_ej extends UnsupportedOperationException implements Vulcan_iX {
   private static final String Vulcan_y;
   private static final long serialVersionUID = -6894122266938754088L;
   private Vulcan_eK Vulcan_q;
   private Throwable Vulcan_m;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1338094318313040931L, -7090342439387667504L, (Object)null).a(129450240604935L);
   private static final String[] b;

   public Vulcan_ej() {
      super(b[2]);
      this.Vulcan_q = new Vulcan_eK(this);
   }

   public Vulcan_ej(String var1) {
      long var2 = a ^ 4326237965099L;
      super(var1 == null ? b[2] : var1);
      this.Vulcan_q = new Vulcan_eK(this);
   }

   public Vulcan_ej(Throwable var1) {
      super(b[2]);
      this.Vulcan_q = new Vulcan_eK(this);
      this.Vulcan_m = var1;
   }

   public Vulcan_ej(String var1, Throwable var2) {
      long var3 = a ^ 107566753171621L;
      super(var1 == null ? b[2] : var1);
      this.Vulcan_q = new Vulcan_eK(this);
      this.Vulcan_m = var2;
   }

   public Vulcan_ej(Class var1) {
      long var2 = a ^ 54001913838331L;
      String var10001;
      if (var1 == null) {
         String[] var4 = b;
         var10001 = var4[1];
      } else {
         var10001 = b[0] + var1;
      }

      super(var10001);
      this.Vulcan_q = new Vulcan_eK(this);
   }

   public Throwable getCause() {
      return this.Vulcan_m;
   }

   public String getMessage() {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_o(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      long var5 = var2 ^ 44954694477396L;

      try {
         if (var4 == 0) {
            return super.getMessage();
         }
      } catch (Vulcan_ej var7) {
         throw a(var7);
      }

      return this.Vulcan_q.Vulcan_e(new Object[]{new Long(var5), new Integer(var4)});
   }

   public String[] Vulcan_K(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 25628550657000L;
      return this.Vulcan_q.Vulcan_A(new Object[]{new Long(var4)});
   }

   public Throwable Vulcan_w(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      long var5 = var3 ^ 112738229874061L;
      return this.Vulcan_q.Vulcan_m(new Object[]{new Long(var5), new Integer(var2)});
   }

   public int Vulcan_x(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 134422025844835L;
      return this.Vulcan_q.Vulcan_N(new Object[]{new Long(var4)});
   }

   public Throwable[] Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 39448265570914L;
      return this.Vulcan_q.Vulcan_W(new Object[]{new Long(var4)});
   }

   public int Vulcan_z(Object[] var1) {
      long var3 = (Long)var1[0];
      Class var2 = (Class)var1[1];
      long var5 = var3 ^ 63460973614450L;
      return this.Vulcan_q.Vulcan_a(new Object[]{var2, new Integer(0), new Long(var5)});
   }

   public int Vulcan_T(Object[] var1) {
      Class var4 = (Class)var1[0];
      long var2 = (Long)var1[1];
      int var5 = (Integer)var1[2];
      long var6 = var2 ^ 6363077939541L;
      return this.Vulcan_q.Vulcan_a(new Object[]{var4, new Integer(var5), new Long(var6)});
   }

   public void printStackTrace() {
      long var1 = a ^ 42720022693040L;
      long var3 = var1 ^ 99012547832723L;
      this.Vulcan_q.Vulcan_k(new Object[]{new Long(var3)});
   }

   public void printStackTrace(PrintStream var1) {
      long var2 = a ^ 117855008034765L;
      long var4 = var2 ^ 9397779873028L;
      this.Vulcan_q.Vulcan_b(new Object[]{var1, new Long(var4)});
   }

   public void printStackTrace(PrintWriter var1) {
      long var2 = a ^ 130943979983714L;
      long var4 = var2 ^ 125403970595297L;
      this.Vulcan_q.Vulcan_s(new Object[]{var1, new Long(var4)});
   }

   public final void Vulcan_T(Object[] var1) {
      PrintWriter var2 = (PrintWriter)var1[0];
      super.printStackTrace(var2);
   }

   static {
      String[] var5 = new String[3];
      int var3 = 0;
      String var2 = "(Ho`\u0014!EKIdq\u0014![\u001bKnhQ&B\u000eC+lZh\u0017(Ho`\u0014!EKIdq\u0014![\u001bKnhQ&B\u000eC\u0017(Ho`\u0014!EKIdq\u0014![\u001bKnhQ&B\u000eC";
      int var4 = "(Ho`\u0014!EKIdq\u0014![\u001bKnhQ&B\u000eC+lZh\u0017(Ho`\u0014!EKIdq\u0014![\u001bKnhQ&B\u000eC\u0017(Ho`\u0014!EKIdq\u0014![\u001bKnhQ&B\u000eC".length();
      char var1 = 27;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 33;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 33;
               var10005 = var7;
            } else {
               var10 = 33;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 33;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 74;
                  break;
               case 1:
                  var23 = 6;
                  break;
               case 2:
                  var23 = 42;
                  break;
               case 3:
                  var23 = 36;
                  break;
               case 4:
                  var23 = 21;
                  break;
               case 5:
                  var23 = 105;
                  break;
               default:
                  var23 = 23;
               }

               var10004[var10005] = (char)(var22 ^ var12 ^ var23);
               ++var7;
               if (var10 == 0) {
                  var10005 = var10;
                  var10004 = var10001;
                  var12 = var10;
               } else {
                  if (var8 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var12 = var10;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            b = var5;
            Vulcan_y = b[2];
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static Vulcan_ej a(Vulcan_ej var0) {
      return var0;
   }
}
