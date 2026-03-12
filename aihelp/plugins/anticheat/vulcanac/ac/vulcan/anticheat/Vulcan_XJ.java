package ac.vulcan.anticheat;

public class Vulcan_XJ extends Number implements Comparable, Vulcan_i8 {
   private static final long serialVersionUID = 5787169186L;
   private float Vulcan_p;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(4927841890147587222L, 8920126964665784201L, (Object)null).a(8268734295452L);

   public Vulcan_XJ() {
   }

   public Vulcan_XJ(float var1) {
      this.Vulcan_p = var1;
   }

   public Vulcan_XJ(Number var1) {
      this.Vulcan_p = var1.floatValue();
   }

   public Vulcan_XJ(String var1) {
      this.Vulcan_p = Float.parseFloat(var1);
   }

   public Object Vulcan_G(Object[] var1) {
      int var3 = (Integer)var1[0];
      int var2 = (Integer)var1[1];
      int var4 = (Integer)var1[2];
      return new Float(this.Vulcan_p);
   }

   public void Vulcan_u(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_p = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_u(new Object[]{new Boolean(((Number)var2).floatValue())});
   }

   public boolean Vulcan_q(Object[] var1) {
      return Float.isNaN(this.Vulcan_p);
   }

   public boolean Vulcan_R(Object[] var1) {
      return Float.isInfinite(this.Vulcan_p);
   }

   public void Vulcan_E(Object[] var1) {
      ++this.Vulcan_p;
   }

   public void Vulcan_K(Object[] var1) {
      --this.Vulcan_p;
   }

   public void Vulcan_t(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_p += var2;
   }

   public void Vulcan_e(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_p += var2.floatValue();
   }

   public void Vulcan_r(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_p -= var2;
   }

   public void Vulcan_z(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_p -= var2.floatValue();
   }

   public int intValue() {
      return (int)this.Vulcan_p;
   }

   public long longValue() {
      return (long)this.Vulcan_p;
   }

   public float floatValue() {
      return this.Vulcan_p;
   }

   public double doubleValue() {
      return (double)this.Vulcan_p;
   }

   public Float Vulcan_g(Object[] var1) {
      return new Float(this.floatValue());
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return Float.floatToIntBits(this.Vulcan_p);
   }

   public int compareTo(Object var1) {
      long var2 = a ^ 117556369097094L;
      long var4 = var2 ^ 96608220294200L;
      Vulcan_XJ var6 = (Vulcan_XJ)var1;
      float var7 = var6.Vulcan_p;
      return Vulcan_bi.Vulcan_Y(new Object[]{new Boolean(this.Vulcan_p), new Long(var4), new Boolean(var7)});
   }

   public String toString() {
      return String.valueOf(this.Vulcan_p);
   }

   private static NumberFormatException a(NumberFormatException var0) {
      return var0;
   }
}
