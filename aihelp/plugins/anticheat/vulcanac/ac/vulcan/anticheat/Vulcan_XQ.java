package ac.vulcan.anticheat;

public class Vulcan_XQ extends Number implements Comparable, Vulcan_i8 {
   private static final long serialVersionUID = 62986528375L;
   private long Vulcan_I;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-3291028397066988774L, 4004474260863799562L, (Object)null).a(163972509387128L);

   public Vulcan_XQ() {
   }

   public Vulcan_XQ(long var1) {
      this.Vulcan_I = var1;
   }

   public Vulcan_XQ(Number var1) {
      this.Vulcan_I = var1.longValue();
   }

   public Vulcan_XQ(String var1) {
      this.Vulcan_I = Long.parseLong(var1);
   }

   public Object Vulcan_G(Object[] var1) {
      int var4 = (Integer)var1[0];
      int var3 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      return new Long(this.Vulcan_I);
   }

   public void Vulcan_x(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_I = var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_x(new Object[]{new Long(((Number)var2).longValue())});
   }

   public void Vulcan_d(Object[] var1) {
      ++this.Vulcan_I;
   }

   public void Vulcan_B(Object[] var1) {
      --this.Vulcan_I;
   }

   public void Vulcan_I(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_I += var2;
   }

   public void Vulcan_r(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_I += var2.longValue();
   }

   public void Vulcan_t(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_I -= var2;
   }

   public void Vulcan_o(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_I -= var2.longValue();
   }

   public int intValue() {
      return (int)this.Vulcan_I;
   }

   public long longValue() {
      return this.Vulcan_I;
   }

   public float floatValue() {
      return (float)this.Vulcan_I;
   }

   public double doubleValue() {
      return (double)this.Vulcan_I;
   }

   public Long Vulcan__(Object[] var1) {
      return new Long(this.longValue());
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return (int)(this.Vulcan_I ^ this.Vulcan_I >>> 32);
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
