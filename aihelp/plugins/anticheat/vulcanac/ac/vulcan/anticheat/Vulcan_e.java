package ac.vulcan.anticheat;

public class Vulcan_e extends Number implements Comparable, Vulcan_i8 {
   private static final long serialVersionUID = 512176391864L;
   private int Vulcan_I;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(1383059421769794198L, -672988531398372193L, (Object)null).a(14293629668885L);

   public Vulcan_e() {
   }

   public Vulcan_e(int var1) {
      this.Vulcan_I = var1;
   }

   public Vulcan_e(Number var1) {
      this.Vulcan_I = var1.intValue();
   }

   public Vulcan_e(String var1) {
      this.Vulcan_I = Integer.parseInt(var1);
   }

   public Object Vulcan_G(Object[] var1) {
      int var3 = (Integer)var1[0];
      int var4 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      return new Integer(this.Vulcan_I);
   }

   public void Vulcan_G(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_I = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_G(new Object[]{new Integer(((Number)var2).intValue())});
   }

   public void Vulcan_K(Object[] var1) {
      ++this.Vulcan_I;
   }

   public void Vulcan_A(Object[] var1) {
      --this.Vulcan_I;
   }

   public void Vulcan_n(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_I += var2;
   }

   public void Vulcan_x(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_I += var2.intValue();
   }

   public void Vulcan_W(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_I -= var2;
   }

   public void Vulcan_L(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_I -= var2.intValue();
   }

   public int intValue() {
      return this.Vulcan_I;
   }

   public long longValue() {
      return (long)this.Vulcan_I;
   }

   public float floatValue() {
      return (float)this.Vulcan_I;
   }

   public double doubleValue() {
      return (double)this.Vulcan_I;
   }

   public Integer Vulcan_B(Object[] var1) {
      return new Integer(this.intValue());
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return this.Vulcan_I;
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      return String.valueOf(this.Vulcan_I);
   }

   private static NumberFormatException a(NumberFormatException var0) {
      return var0;
   }
}
