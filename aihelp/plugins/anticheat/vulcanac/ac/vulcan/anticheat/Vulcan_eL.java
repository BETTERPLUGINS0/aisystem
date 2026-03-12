package ac.vulcan.anticheat;

public class Vulcan_eL extends Number implements Comparable, Vulcan_i8 {
   private static final long serialVersionUID = -1585823265L;
   private byte Vulcan_X;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(2103760101118120729L, 3098828812516950980L, (Object)null).a(125668882137854L);

   public Vulcan_eL() {
   }

   public Vulcan_eL(byte var1) {
      this.Vulcan_X = var1;
   }

   public Vulcan_eL(Number var1) {
      this.Vulcan_X = var1.byteValue();
   }

   public Vulcan_eL(String var1) {
      this.Vulcan_X = Byte.parseByte(var1);
   }

   public Object Vulcan_G(Object[] var1) {
      int var4 = (Integer)var1[0];
      int var3 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      return new Byte(this.Vulcan_X);
   }

   public void Vulcan_T(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_X = (byte)var2;
   }

   public void Vulcan_Z(Object[] var1) {
      Object var2 = (Object)var1[0];
      this.Vulcan_T(new Object[]{new Integer(((Number)var2).byteValue())});
   }

   public void Vulcan_V(Object[] var1) {
      ++this.Vulcan_X;
   }

   public void Vulcan_U(Object[] var1) {
      --this.Vulcan_X;
   }

   public void Vulcan_P(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_X = (byte)(this.Vulcan_X + var2);
   }

   public void Vulcan_b(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_X += var2.byteValue();
   }

   public void Vulcan_X(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_X = (byte)(this.Vulcan_X - var2);
   }

   public void Vulcan_E(Object[] var1) {
      Number var2 = (Number)var1[0];
      this.Vulcan_X -= var2.byteValue();
   }

   public byte byteValue() {
      return this.Vulcan_X;
   }

   public int intValue() {
      return this.Vulcan_X;
   }

   public long longValue() {
      return (long)this.Vulcan_X;
   }

   public float floatValue() {
      return (float)this.Vulcan_X;
   }

   public double doubleValue() {
      return (double)this.Vulcan_X;
   }

   public Byte Vulcan_i(Object[] var1) {
      return new Byte(this.byteValue());
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return this.Vulcan_X;
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      return String.valueOf(this.Vulcan_X);
   }

   private static NumberFormatException a(NumberFormatException var0) {
      return var0;
   }
}
