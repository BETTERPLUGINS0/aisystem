package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.HashCommon;
import java.util.Set;

public abstract class AbstractFloatSet extends AbstractFloatCollection implements Cloneable, FloatSet {
   protected AbstractFloatSet() {
   }

   public abstract FloatIterator iterator();

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
            return s instanceof FloatSet ? this.containsAll((FloatSet)s) : this.containsAll(s);
         }
      }
   }

   public int hashCode() {
      int h = 0;
      int n = this.size();

      float k;
      for(FloatIterator i = this.iterator(); n-- != 0; h += HashCommon.float2int(k)) {
         k = i.nextFloat();
      }

      return h;
   }

   public boolean remove(float k) {
      return super.rem(k);
   }

   /** @deprecated */
   @Deprecated
   public boolean rem(float k) {
      return this.remove(k);
   }
}
