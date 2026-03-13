package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math;

public abstract class Encoding {
   protected Field f;

   public synchronized void setField(Field f) {
      if (this.f != null) {
         throw new IllegalStateException("already set");
      } else {
         this.f = f;
      }
   }

   public abstract byte[] encode(FieldElement var1);

   public abstract FieldElement decode(byte[] var1);

   public abstract boolean isNegative(FieldElement var1);
}
