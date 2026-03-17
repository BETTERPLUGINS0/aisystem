package me.PM2.infinitevehicles.xseries;

public abstract class AbstractReferencedClass<T> {
   public abstract T object();

   protected final Object toVirtual() {
      return this.object();
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         return this.getClass().isInstance(var1.getClass()) ? this.toVirtual().equals(((AbstractReferencedClass)var1).toVirtual()) : this.object().equals(var1);
      }
   }

   public final int hashCode() {
      return this.toVirtual().hashCode();
   }

   public final String toString() {
      return this.getClass().getSimpleName() + '(' + this.toVirtual().toString() + ')';
   }
}
