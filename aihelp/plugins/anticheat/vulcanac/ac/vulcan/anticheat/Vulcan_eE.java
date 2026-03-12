package ac.vulcan.anticheat;

public class Vulcan_eE extends Number implements Comparable, Vulcan_i8 {
   private static final long serialVersionUID = 1587163916L;
   private double Vulcan_g;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(6001191820987545104L, 5633205498141180346L, (Object)null).a(73096784443974L);

   public Vulcan_eE() {
   }

   public Vulcan_eE(double var1) {
      this.Vulcan_g = var1;
   }

   public Vulcan_eE(Number var1) {
      this.Vulcan_g = var1.doubleValue();
   }

   public Vulcan_eE(String var1) {
      this.Vulcan_g = Double.parseDouble(var1);
   }

   public Object Vulcan_G(Object[] var1) {
      int var2 = (Integer)var1[0];
      int var4 = (Integer)var1[1];
      int var3 = (Integer)var1[2];
      return new Double(this.Vulcan_g);
   }

   public void Vulcan_E(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_g = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_E(new Object[]{new Double(((Number)var2).doubleValue())});
   }

   public boolean Vulcan_W(Object[] var1) {
      return Double.isNaN(this.Vulcan_g);
   }

   public boolean Vulcan_v(Object[] var1) {
      return Double.isInfinite(this.Vulcan_g);
   }

   public void Vulcan_z(Object[] var1) {
      ++this.Vulcan_g;
   }

   public void Vulcan_P(Object[] var1) {
      --this.Vulcan_g;
   }

   public void Vulcan_h(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_g += var2;
   }

   public void Vulcan_D(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_g += var2.doubleValue();
   }

   public void Vulcan_r(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_g -= var2;
   }

   public void Vulcan_f(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_g -= var2.doubleValue();
   }

   public int intValue() {
      return (int)this.Vulcan_g;
   }

   public long longValue() {
      return (long)this.Vulcan_g;
   }

   public float floatValue() {
      return (float)this.Vulcan_g;
   }

   public double doubleValue() {
      return this.Vulcan_g;
   }

   public Double Vulcan_k(Object[] var1) {
      return new Double(this.doubleValue());
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.Vulcan_g);
      return (int)(var1 ^ var1 >>> 32);
   }

   public int compareTo(Object var1) {
      long var2 = a ^ 78496635440653L;
      long var4 = var2 ^ 50687738755210L;
      Vulcan_eE var6 = (Vulcan_eE)var1;
      double var7 = var6.Vulcan_g;
      return Vulcan_bi.Vulcan__(new Object[]{new Double(this.Vulcan_g), new Long(var4), new Double(var7)});
   }

   public String toString() {
      return String.valueOf(this.Vulcan_g);
   }

   private static NumberFormatException a(NumberFormatException var0) {
      return var0;
   }
}
