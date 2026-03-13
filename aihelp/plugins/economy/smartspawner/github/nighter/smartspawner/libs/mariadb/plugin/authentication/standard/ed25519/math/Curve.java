package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math;

import java.io.Serializable;

public class Curve implements Serializable {
   private static final long serialVersionUID = 4578920872509827L;
   private final Field f;
   private final FieldElement d;
   private final FieldElement d2;
   private final FieldElement I;
   private final GroupElement zeroP2;
   private final GroupElement zeroP3;
   private final GroupElement zeroP3PrecomputedDouble;
   private final GroupElement zeroPrecomp;

   public Curve(Field f, byte[] d, FieldElement I) {
      this.f = f;
      this.d = f.fromByteArray(d);
      this.d2 = this.d.add(this.d);
      this.I = I;
      FieldElement zero = f.ZERO;
      FieldElement one = f.ONE;
      this.zeroP2 = GroupElement.p2(this, zero, one, one);
      this.zeroP3 = GroupElement.p3(this, zero, one, one, zero, false);
      this.zeroP3PrecomputedDouble = GroupElement.p3(this, zero, one, one, zero, true);
      this.zeroPrecomp = GroupElement.precomp(this, one, one, zero);
   }

   public Field getField() {
      return this.f;
   }

   public FieldElement getD() {
      return this.d;
   }

   public FieldElement get2D() {
      return this.d2;
   }

   public FieldElement getI() {
      return this.I;
   }

   public GroupElement getZero(GroupElement.Representation repr) {
      switch(repr) {
      case P2:
         return this.zeroP2;
      case P3:
         return this.zeroP3;
      case P3PrecomputedDouble:
         return this.zeroP3PrecomputedDouble;
      case PRECOMP:
         return this.zeroPrecomp;
      default:
         return null;
      }
   }

   public GroupElement createPoint(byte[] P, boolean precompute) {
      return new GroupElement(this, P, precompute);
   }

   public int hashCode() {
      return this.f.hashCode() ^ this.d.hashCode() ^ this.I.hashCode();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Curve)) {
         return false;
      } else {
         Curve c = (Curve)o;
         return this.f.equals(c.getField()) && this.d.equals(c.getD()) && this.I.equals(c.getI());
      }
   }
}
