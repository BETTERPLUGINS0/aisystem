package ac.vulcan.anticheat;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class Vulcan_bT extends Format {
   private static final long serialVersionUID = -4329119827877627683L;
   private final Format Vulcan_m;
   private final Format Vulcan_a;

   public Vulcan_bT(Format var1, Format var2) {
      this.Vulcan_m = var1;
      this.Vulcan_a = var2;
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      return this.Vulcan_a.format(var1, var2, var3);
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.Vulcan_m.parseObject(var1, var2);
   }

   public Format Vulcan_M(Object[] var1) {
      return this.Vulcan_m;
   }

   public Format Vulcan__(Object[] var1) {
      return this.Vulcan_a;
   }

   public String Vulcan_Z(Object[] var1) {
      String var2 = (String)var1[0];
      return this.format(this.parseObject(var2));
   }
}
