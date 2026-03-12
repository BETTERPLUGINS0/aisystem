package ac.vulcan.anticheat;

public class Vulcan_ep {
   private final int Vulcan_O;
   private final int Vulcan_G;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(2851524605321986256L, 1784867838199927188L, (Object)null).a(11163818270724L);

   public Vulcan_ep(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_Z(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_s(new Object[]{new Integer(var2)}) >> this.Vulcan_G;
   }

   public short Vulcan_H(Object[] var1) {
      int var2 = (Integer)var1[0];
      return (short)this.Vulcan_Z(new Object[]{new Integer(var2)});
   }

   public int Vulcan_s(Object[] var1) {
      int var2 = (Integer)var1[0];
      return var2 & this.Vulcan_O;
   }

   public short Vulcan_A(Object[] var1) {
      int var2 = (Integer)var1[0];
      return (short)this.Vulcan_s(new Object[]{new Integer(var2)});
   }

   public boolean Vulcan_N(Object[] var1) {
      int var4 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      String[] var5 = Vulcan_XL.Vulcan_v();

      int var7;
      label32: {
         try {
            var7 = var4 & this.Vulcan_O;
            if (var5 == null) {
               return (boolean)var7;
            }

            if (var7 != 0) {
               break label32;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var7 = 0;
         return (boolean)var7;
      }

      var7 = 1;
      return (boolean)var7;
   }

   public boolean Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_D(Object[] var1) {
      int var3 = (Integer)var1[0];
      int var2 = (Integer)var1[1];
      return var3 & ~this.Vulcan_O | var2 << this.Vulcan_G & this.Vulcan_O;
   }

   public short Vulcan_C(Object[] var1) {
      int var3 = (Integer)var1[0];
      int var2 = (Integer)var1[1];
      return (short)this.Vulcan_D(new Object[]{new Integer(var3), new Integer(var2)});
   }

   public int Vulcan_C(Object[] var1) {
      int var2 = (Integer)var1[0];
      return var2 & ~this.Vulcan_O;
   }

   public short Vulcan_Y(Object[] var1) {
      int var2 = (Integer)var1[0];
      return (short)this.Vulcan_C(new Object[]{new Integer(var2)});
   }

   public byte Vulcan_W(Object[] var1) {
      int var2 = (Integer)var1[0];
      return (byte)this.Vulcan_C(new Object[]{new Integer(var2)});
   }

   public int Vulcan_L(Object[] var1) {
      int var2 = (Integer)var1[0];
      return var2 | this.Vulcan_O;
   }

   public short Vulcan_D(Object[] var1) {
      int var2 = (Integer)var1[0];
      return (short)this.Vulcan_L(new Object[]{new Integer(var2)});
   }

   public byte Vulcan_X(Object[] var1) {
      int var2 = (Integer)var1[0];
      return (byte)this.Vulcan_L(new Object[]{new Integer(var2)});
   }

   public int Vulcan_M(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public short Vulcan_z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public byte Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
