package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.HashCommon;
import ac.grim.grimac.shaded.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

public class ObjectDoubleImmutablePair<K> implements ObjectDoublePair<K>, Serializable {
   private static final long serialVersionUID = 0L;
   protected final K left;
   protected final double right;

   public ObjectDoubleImmutablePair(K left, double right) {
      this.left = left;
      this.right = right;
   }

   public static <K> ObjectDoubleImmutablePair<K> of(K left, double right) {
      return new ObjectDoubleImmutablePair(left, right);
   }

   public K left() {
      return this.left;
   }

   public double rightDouble() {
      return this.right;
   }

   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else if (other instanceof ObjectDoublePair) {
         return Objects.equals(this.left, ((ObjectDoublePair)other).left()) && this.right == ((ObjectDoublePair)other).rightDouble();
      } else if (!(other instanceof Pair)) {
         return false;
      } else {
         return Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
      }
   }

   public int hashCode() {
      return (this.left == null ? 0 : this.left.hashCode()) * 19 + HashCommon.double2int(this.right);
   }

   public String toString() {
      return "<" + this.left() + "," + this.rightDouble() + ">";
   }
}
