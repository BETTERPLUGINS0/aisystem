package ac.vulcan.anticheat;

import java.util.Calendar;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Vulcan_X9 implements Iterator {
   private final Calendar Vulcan_i;
   private final Calendar Vulcan_c;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(6831352778931279649L, -7812917470370258845L, (Object)null).a(36400259786189L);

   Vulcan_X9(Calendar var1, Calendar var2) {
      this.Vulcan_i = var2;
      this.Vulcan_c = var1;
      this.Vulcan_c.add(5, -1);
   }

   public boolean hasNext() {
      return this.Vulcan_c.before(this.Vulcan_i);
   }

   public Object next() {
      // $FF: Couldn't be decompiled
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   private static NoSuchElementException a(NoSuchElementException var0) {
      return var0;
   }
}
