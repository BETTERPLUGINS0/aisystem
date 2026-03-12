package ac.vulcan.anticheat;

public class Vulcan_XA extends Number implements Comparable, Vulcan_i8 {
   private static final long serialVersionUID = -2135791679L;
   private short Vulcan_O;
   private static String[] Vulcan_H;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-4544373985888225549L, -764169587063131656L, (Object)null).a(61742366899776L);

   public Vulcan_XA() {
   }

   public Vulcan_XA(short var1) {
      this.Vulcan_O = var1;
   }

   public Vulcan_XA(Number var1) {
      this.Vulcan_O = var1.shortValue();
   }

   public Vulcan_XA(String var1) {
      this.Vulcan_O = Short.parseShort(var1);
   }

   public Object Vulcan_G(Object[] var1) {
      int var4 = (Integer)var1[0];
      int var3 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      return new Short(this.Vulcan_O);
   }

   public void Vulcan_j(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O = (short)var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_j(new Object[]{new Integer(((Number)var2).shortValue())});
   }

   public void Vulcan_P(Object[] var1) {
      ++this.Vulcan_O;
   }

   public void Vulcan_V(Object[] var1) {
      --this.Vulcan_O;
   }

   public void Vulcan_N(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O = (short)(this.Vulcan_O + var2);
   }

   public void Vulcan_e(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_O += var2.shortValue();
   }

   public void Vulcan_M(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_O = (short)(this.Vulcan_O - var2);
   }

   public void Vulcan_f(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_O -= var2.shortValue();
   }

   public short shortValue() {
      return this.Vulcan_O;
   }

   public int intValue() {
      return this.Vulcan_O;
   }

   public long longValue() {
      return (long)this.Vulcan_O;
   }

   public float floatValue() {
      return (float)this.Vulcan_O;
   }

   public double doubleValue() {
      return (double)this.Vulcan_O;
   }

   public Short Vulcan_R(Object[] var1) {
      return new Short(this.shortValue());
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return this.Vulcan_O;
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      return String.valueOf(this.Vulcan_O);
   }

   public static void Vulcan_n(String[] var0) {
      Vulcan_H = var0;
   }

   public static String[] Vulcan_c() {
      return Vulcan_H;
   }

   static {
      if (Vulcan_c() != null) {
         Vulcan_n(new String[3]);
      }

   }

   private static NumberFormatException a(NumberFormatException var0) {
      return var0;
   }
}
