package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.HashCommon;
import java.util.Set;

public abstract class AbstractDoubleSet extends AbstractDoubleCollection implements Cloneable, DoubleSet {
   protected AbstractDoubleSet() {
   }

   public abstract DoubleIterator iterator();

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Set)) {
         return false;
      } else {
         Set<?> s = (Set)o;
         if (s.size() != this.size()) {
            return false;
         } else {
            return s instanceof DoubleSet ? this.containsAll((DoubleSet)s) : this.containsAll(s);
         }
      }
   }

   public int hashCode() {
      int h = 0;
      int n = this.size();

      double k;
      for(DoubleIterator i = this.iterator(); n-- != 0; h += HashCommon.double2int(k)) {
         k = i.nextDouble();
      }

      return h;
   }

   public boolean remove(double k) {
      return super.rem(k);
   }

   /** @deprecated */
   @Deprecated
   public boolean rem(double k) {
      return this.remove(k);
   }
}
