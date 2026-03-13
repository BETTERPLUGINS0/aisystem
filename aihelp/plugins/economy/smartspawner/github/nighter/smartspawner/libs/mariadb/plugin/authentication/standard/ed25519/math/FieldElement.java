package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math;

import java.io.Serializable;

public abstract class FieldElement implements Serializable {
   private static final long serialVersionUID = 1239527465875676L;
   protected final Field f;

   public FieldElement(Field f) {
      if (null == f) {
         throw new IllegalArgumentException("field cannot be null");
      } else {
         this.f = f;
      }
   }

   public byte[] toByteArray() {
      return this.f.getEncoding().encode(this);
   }

   public abstract boolean isNonZero();

   public boolean isNegative() {
      return this.f.getEncoding().isNegative(this);
   }

   public abstract FieldElement add(FieldElement var1);

   public FieldElement addOne() {
      return this.add(this.f.ONE);
   }

   public abstract FieldElement subtract(FieldElement var1);

   public FieldElement subtractOne() {
      return this.subtract(this.f.ONE);
   }

   public abstract FieldElement negate();

   public FieldElement divide(FieldElement val) {
      return this.multiply(val.invert());
   }

   public abstract FieldElement multiply(FieldElement var1);

   public abstract FieldElement square();

   public abstract FieldElement squareAndDouble();

   public abstract FieldElement invert();

   public abstract FieldElement pow22523();

   public abstract FieldElement cmov(FieldElement var1, int var2);
}
